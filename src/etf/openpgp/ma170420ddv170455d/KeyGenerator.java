package etf.openpgp.ma170420ddv170455d;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

public class KeyGenerator {
	private PGPPublicKeyRingCollection publicKeyRingCollection;
	private PGPSecretKeyRingCollection secretKeyRingCollection;
	
	private static File publicKeyRingDirectory;
    private static File secretKeyRingDirectory;
	
	public KeyGenerator(String path) throws IOException, PGPException {
		publicKeyRingDirectory = new File(path + "\\publicKeyRing.asc");
		secretKeyRingDirectory = new File(path + "\\secretKeyRing.asc");
		
		publicKeyRingCollection = new PGPPublicKeyRingCollection(new ArmoredInputStream(new FileInputStream(publicKeyRingDirectory)), new BcKeyFingerprintCalculator());
		secretKeyRingCollection = new PGPSecretKeyRingCollection(new ArmoredInputStream(new FileInputStream(secretKeyRingDirectory)), new BcKeyFingerprintCalculator());
		
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public void generateKeys(String name, String mail, String password, int keySize) throws PGPException, NoSuchAlgorithmException, IOException {
		// key pair
		KeyPairGenerator keyPairGeneratorRsa = KeyPairGenerator.getInstance("RSA");
		keyPairGeneratorRsa.initialize(keySize);
		KeyPair masterKeyPair = keyPairGeneratorRsa.generateKeyPair();
		KeyPair keyPair = keyPairGeneratorRsa.generateKeyPair();
		PGPKeyPair pgpMasterKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_SIGN, masterKeyPair, new Date());
		PGPKeyPair pgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, keyPair, new Date());
		 
		// za hash
		PGPDigestCalculator sha1DigestCalculator = new JcaPGPDigestCalculatorProviderBuilder()
				.build().get(HashAlgorithmTags.SHA1);
		 
		PGPKeyRingGenerator keyRingGenerator = new PGPKeyRingGenerator(
				PGPSignature.POSITIVE_CERTIFICATION, pgpMasterKeyPair, name + "#" + mail, 
				sha1DigestCalculator, null, null,
				new JcaPGPContentSignerBuilder(PGPPublicKey.RSA_SIGN, HashAlgorithmTags.SHA1),
				new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha1DigestCalculator)
					.setProvider("BC").build(password.toCharArray())
		);
		
		keyRingGenerator.addSubKey(pgpKeyPair);
		
		PGPSecretKeyRing privateKeyRing = keyRingGenerator.generateSecretKeyRing();
		PGPPublicKeyRing publicKeyRing = keyRingGenerator.generatePublicKeyRing();
		
		secretKeyRingCollection = PGPSecretKeyRingCollection.addSecretKeyRing(secretKeyRingCollection, privateKeyRing);
		publicKeyRingCollection = PGPPublicKeyRingCollection.addPublicKeyRing(publicKeyRingCollection, publicKeyRing);
		
		ArmoredOutputStream aos1 = new ArmoredOutputStream(new FileOutputStream(secretKeyRingDirectory));
		secretKeyRingCollection.encode(aos1);
        aos1.close();
        
        ArmoredOutputStream aos2 = new ArmoredOutputStream(new FileOutputStream(publicKeyRingDirectory));
        publicKeyRingCollection.encode(aos2);
        aos2.close();
	}
	
	public PGPSecretKeyRing getPrivateRing(int index) {
		PGPSecretKeyRing currentRing = null;
		java.util.Iterator<PGPSecretKeyRing> iter = secretKeyRingCollection.getKeyRings();
		for (int i = 0; i < index + 1; i++) {
			if (!iter.hasNext()) return null;
			currentRing = iter.next();
		}
		return currentRing;
	}
	
	public PGPPublicKeyRing getPublicRing(int index) {
		PGPPublicKeyRing currentRing = null;
		java.util.Iterator<PGPPublicKeyRing> iter = publicKeyRingCollection.getKeyRings();
		for (int i = 0; i < index + 1; i++) {
			if (!iter.hasNext()) return null;
			currentRing = iter.next();
		}
		return currentRing;
	}
	
//	public void exportKey(long id) {
//		exportPrivateKey(id);
//		exportPublicKey(id);
//	}
	
	public PGPPublicKeyRing findPublicRing(long id) throws Exception {
		Iterator<PGPPublicKeyRing> iter = publicKeyRingCollection.getKeyRings();
		PGPPublicKeyRing ring = null;
		boolean foundKey = false;
		while (iter.hasNext() && !foundKey) {
			ring = iter.next();
			Iterator<PGPPublicKey> iterKey = ring.getPublicKeys();
			while (iterKey.hasNext()) {
				PGPPublicKey key = iterKey.next();
				if (key.getKeyID() == id) {
					foundKey = true;
					break;
				}
			}
		}
		if (!foundKey) throw new Exception("Nema takvog kljuca");
		else return ring;
	}
	
	public PGPSecretKeyRing findSecretRing(long id) throws Exception {
		Iterator<PGPSecretKeyRing> iter = secretKeyRingCollection.getKeyRings();
		PGPSecretKeyRing ring = null;
		boolean foundKey = false;
		while (iter.hasNext() && !foundKey) {
			ring = iter.next();
			Iterator<PGPSecretKey> iterKey = ring.getSecretKeys();
			while (iterKey.hasNext()) {
				PGPSecretKey key = iterKey.next();
				if (key.getKeyID() == id) {
					foundKey = true;
					break;
				}
			}
		}
		if (!foundKey) throw new Exception("Nema takvog kljuca");
		else return ring;
	}
	
	public void exportPublicKey(long id, String path) {
		try {
			PGPPublicKeyRing ring = findPublicRing(id);
			ArmoredOutputStream aos = new ArmoredOutputStream(new FileOutputStream(new File(path)));
			ring.encode(aos);
            aos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void exportPrivateKey(long id, String path) {
		try {
			PGPSecretKeyRing ring = findSecretRing(id);
			ArmoredOutputStream aos = new ArmoredOutputStream(new FileOutputStream(new File(path)));
			ring.encode(aos);
            aos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importPublicKey(String path) {
		ArmoredInputStream ais;
		try {
			ais = new ArmoredInputStream(new FileInputStream(new File(path)));
			PGPPublicKeyRingCollection newCollection = new PGPPublicKeyRingCollection(ais, new BcKeyFingerprintCalculator());
			Iterator<PGPPublicKeyRing> iterRing = newCollection.getKeyRings();
			while (iterRing.hasNext()) {
				PGPPublicKeyRing ring = iterRing.next();
				publicKeyRingCollection = PGPPublicKeyRingCollection.addPublicKeyRing(publicKeyRingCollection, ring);
				ArmoredOutputStream aos2 = new ArmoredOutputStream(new FileOutputStream(publicKeyRingDirectory));
		        publicKeyRingCollection.encode(aos2);
		        aos2.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importPrivateKey(String path) {
		ArmoredInputStream ais;
		try {
			ais = new ArmoredInputStream(new FileInputStream(new File(path)));
			PGPSecretKeyRingCollection newCollection = new PGPSecretKeyRingCollection(ais, new BcKeyFingerprintCalculator());
			Iterator<PGPSecretKeyRing> iterRing = newCollection.getKeyRings();
			while (iterRing.hasNext()) {
				PGPSecretKeyRing ring = iterRing.next();
				secretKeyRingCollection = PGPSecretKeyRingCollection.addSecretKeyRing(secretKeyRingCollection, ring);
				ArmoredOutputStream aos2 = new ArmoredOutputStream(new FileOutputStream(secretKeyRingDirectory));
				secretKeyRingCollection.encode(aos2);
		        aos2.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
