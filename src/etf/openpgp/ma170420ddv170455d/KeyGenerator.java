package etf.openpgp.ma170420ddv170455d;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;

public class KeyGenerator {
	private PGPPublicKeyRingCollection publicKeyRingCollection;
	private PGPSecretKeyRingCollection secretKeyRingCollection;
	
	private String publicKeyRingDirectory;
	private String secretKeyRingDirectory;
	
	public KeyGenerator(String path) throws IOException, PGPException {
		publicKeyRingDirectory = path + "\\publicKeyRing.bin";
		secretKeyRingDirectory = path + "\\secretcKeyRing.bin";
		
		File publicKeyRingFile = new File(publicKeyRingDirectory);
		if (publicKeyRingFile.exists()) {
			FileInputStream inputStream = new FileInputStream(publicKeyRingFile);
			publicKeyRingCollection = new PGPPublicKeyRingCollection(inputStream, new JcaKeyFingerprintCalculator());
		}
		
		File secretKeyRingFile = new File(secretKeyRingDirectory);
		if (secretKeyRingFile.exists()) {
			FileInputStream inputStream = new FileInputStream(secretKeyRingFile);
			secretKeyRingCollection = new PGPSecretKeyRingCollection(inputStream, new JcaKeyFingerprintCalculator());
		}
		
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public void generateKeys(String name, String mail, String password, int keySize) throws PGPException, NoSuchAlgorithmException, IOException {
		// key pair
		KeyPairGenerator keyPairGeneratorRsa = KeyPairGenerator.getInstance("RSA");
		keyPairGeneratorRsa.initialize(keySize);
		KeyPair keyPair = keyPairGeneratorRsa.generateKeyPair();
		 
		// master key pair (KOJIM OVO ALGORITMOM TREBA??)
		KeyPair masterKeyPair = keyPairGeneratorRsa.generateKeyPair();
		
		// I ovo isto?
		PGPKeyPair pgpMasterKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, masterKeyPair, new Date());
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
		 
		secretKeyRingCollection = PGPSecretKeyRingCollection.addSecretKeyRing(secretKeyRingCollection, privateKeyRing);
		
		OutputStream outputStream = new FileOutputStream(secretKeyRingDirectory);
		BufferedOutputStream secretOut = new BufferedOutputStream(outputStream);
		secretKeyRingCollection.encode(secretOut);
		secretOut.close();
	}

}
