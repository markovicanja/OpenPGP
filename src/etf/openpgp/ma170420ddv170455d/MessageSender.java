package etf.openpgp.ma170420ddv170455d;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

public class MessageSender {

	private KeyGenerator keyGenerator;
	
	private byte[] message;
	
	private long privateKeyID;
	
	private PGPPublicKey[] encryptionKeys;
	
	public MessageSender(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
    public byte[] dataCompression(byte[] message) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
        OutputStream compressedOutputStream = comData.open(bOut);
        compressedOutputStream.write(message);
    	compressedOutputStream.close();    	
    	message = bOut.toByteArray();
    	bOut.close();
        return message;
    }

    public byte[] radixConversion(byte[] message) throws IOException {
    	ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(byteOutputStream);
        armoredOutputStream.write(message);
        armoredOutputStream.close();
        message = byteOutputStream.toByteArray();        
        byteOutputStream.close();
        return message;
    }
    
    public byte[] encrypt(byte[] data, PGPPublicKey[] encryptionKeys, int algorithm) throws IOException, PGPException {
        PGPEncryptedDataGenerator encryptionGenerator = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(algorithm)
                .setWithIntegrityPacket(true)
                .setSecureRandom(new SecureRandom())
                .setProvider("BC"));
        
        for (PGPPublicKey encryptionKey : encryptionKeys) {
        	encryptionGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encryptionKey).setProvider("BC"));
	    }
        
        OutputStream outputStream = new ByteArrayOutputStream();
        OutputStream encryptedOutputStream = encryptionGenerator.open(outputStream, data.length);
        encryptedOutputStream.write(data);
        data = ((ByteArrayOutputStream) outputStream).toByteArray();
        encryptedOutputStream.close();
        outputStream.close();
        return data;
    }
    
    // masterKey je master kljuc
    public byte[] sign(byte[] data, PGPSecretKey masterKey, PGPPrivateKey privateKey) throws PGPException, IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        BCPGOutputStream bcpgos = new BCPGOutputStream(byteStream);        
        
        PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(
        		new JcaPGPContentSignerBuilder(masterKey.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1)
        		.setProvider("BC"));
        signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, privateKey);
        signatureGenerator.generateOnePassVersion(false).encode(bcpgos);
        
        PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
        OutputStream outputStream = literalDataGenerator.open(bcpgos, PGPLiteralData.BINARY, "_CONSOLE", data.length, new Date());

        for(int i = 0; i < data.length; i++) {
        	outputStream.write(data[i]);
        	signatureGenerator.update(data[i]);
        }

        literalDataGenerator.close();
        signatureGenerator.generate().encode(bcpgos);
        data = byteStream.toByteArray();
        byteStream.close();
        bcpgos.close();
        return data;
    }
	
	public void sendMessage(String messagePath, String destPath, boolean auth, boolean encrypt, int encryptAlgorithm,
			boolean zip, boolean radix, char[] pass) throws PGPException, IOException, NoSuchProviderException  {	
		
		message = Files.readAllBytes(Paths.get(messagePath));
    	
    	PGPSecretKey masterKey = null;  
    	PGPPrivateKey privateKey = null;
    	if (auth) {
    		PGPSecretKeyRing ring = keyGenerator.findSecretRing(privateKeyID);
    		Iterator<PGPSecretKey> iterKey = ring.getSecretKeys();
    		while (iterKey.hasNext()) {
    			PGPSecretKey key = iterKey.next();
    			if (key.getKeyID() == privateKeyID) {
    				masterKey = key;
    				break;
    			}
    		}
    		try {
    			privateKey = masterKey.extractPrivateKey(
                        new JcePBESecretKeyDecryptorBuilder()
                        .setProvider("BC")
                        .build(pass));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Pogre\u0161na lozinka za privatni klju\u010D!");
				return;
			}    		
    	}    	
    	
    	if (auth) message = sign(message, masterKey, privateKey);
    	if (zip) message = dataCompression(message);
    	if (encrypt) message = encrypt(message, encryptionKeys, encryptAlgorithm);
    	if (radix) message = radixConversion(message);
    	
    	try (FileOutputStream fos = new FileOutputStream(destPath)) {
    		   fos.write(message);
    	}	
    	
    	JOptionPane.showMessageDialog(null, "Poruka je poslata!");
	}
	
	public void setPrivateKeyID(long keyId) {
		privateKeyID = keyId;
	}
	
	public void setEncryptionKeys(PGPPublicKey[] keys) {
		encryptionKeys = keys;
	}
	
}
