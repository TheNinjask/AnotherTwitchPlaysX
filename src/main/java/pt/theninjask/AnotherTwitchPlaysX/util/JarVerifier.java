package pt.theninjask.AnotherTwitchPlaysX.util;

import java.io.File;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import pt.theninjask.AnotherTwitchPlaysX.exception.NotVerifyJarException;

public class JarVerifier {

	//Most of the code came from
	
	//Read: https://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/HowToImplAProvider.html
	//Code: https://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/MyJCE.java
	
	private static JarVerifier singleton = new JarVerifier();

	private List<Certificate> main;

	private List<Certificate> thirdParty;

	public enum ModType {
		MAIN, THIRD_PARTY, UNKNOWN;
	}

	private JarVerifier() {
		main = new ArrayList<Certificate>();
		thirdParty = new ArrayList<Certificate>();
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			main.add(factory.generateCertificate(JarVerifier.class.getResourceAsStream("/pt/theninjask/AnotherTwitchPlaysX/resource/certificate/app.cer")));
			thirdParty.add(factory.generateCertificate(JarVerifier.class.getResourceAsStream("/pt/theninjask/AnotherTwitchPlaysX/resource/certificate/third_party.cer")));
		} catch (Exception e) {}
	}

	public static JarVerifier getInstance() {
		return singleton;
	}

	public ModType verifyJar(File jarFile) {
		try (JarFile jar = new JarFile(jarFile)) {

			Vector<JarEntry> entriesVec = new Vector<JarEntry>();

			// Ensure the jar file is signed.
			Manifest man = jar.getManifest();
			if (man == null) {
				throw new SecurityException("The provider is not signed");
			}

			// Ensure all the entries' signatures verify correctly
			byte[] buffer = new byte[8192];
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry je = entries.nextElement();

				// Skip directories.
				if (je.isDirectory())
					continue;

				entriesVec.addElement(je);
				InputStream is = jar.getInputStream(je);

				// Read in each jar entry. A security exception will
				// be thrown if a signature/digest check fails.
				while (is.read(buffer, 0, buffer.length) != -1) {
					// Don't care
				}
				is.close();

			}
			Enumeration<JarEntry> e = entriesVec.elements();

			while (e.hasMoreElements()) {
				JarEntry je = e.nextElement();

				// Every file must be signed except files in META-INF.
				Certificate[] certs = je.getCertificates();
				if ((certs == null) || (certs.length == 0)) {
					if (!je.getName().startsWith("META-INF"))
						return ModType.UNKNOWN;
				} else {
					// Check whether the file is signed by the expected
					// signer. The jar may be signed by multiple signers.
					// See if one of the signers is 'targetCert'.
					int startIndex = 0;
					X509Certificate[] certChain;

					while ((certChain = getAChain(certs, startIndex)) != null) {
						Certificate cert=certChain[0];
						if(main.stream().filter(elem->{
							return cert.equals(elem);
						}).count()>0)
							return ModType.MAIN;
						else if(thirdParty.stream().filter(elem->{
							return cert.equals(elem);
						}).count()>0)
							return ModType.THIRD_PARTY;
						// Proceed to the next chain.
						startIndex += certChain.length;
					}

				}
			}

			return ModType.UNKNOWN;
		} catch (Exception e) {
			throw new NotVerifyJarException();
		}
	}

	private static X509Certificate[] getAChain(Certificate[] certs, int startIndex) {
		if (startIndex > certs.length - 1)
			return null;

		int i;
		// Keep going until the next certificate is not the
		// issuer of this certificate.
		for (i = startIndex; i < certs.length - 1; i++) {
			if (!((X509Certificate) certs[i + 1]).getSubjectX500Principal().equals(((X509Certificate) certs[i]).getIssuerX500Principal())) {
				break;
			}
		}
		// Construct and return the found certificate chain.
		int certChainSize = (i - startIndex) + 1;
		X509Certificate[] ret = new X509Certificate[certChainSize];
		for (int j = 0; j < certChainSize; j++) {
			ret[j] = (X509Certificate) certs[startIndex + j];
		}
		return ret;
	}

}
