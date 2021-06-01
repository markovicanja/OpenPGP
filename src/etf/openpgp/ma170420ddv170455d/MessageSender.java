package etf.openpgp.ma170420ddv170455d;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;

public class MessageSender {

	private KeyGenerator keyGenerator;
	
	private byte[] message;
	
	private long privateKeyID;
	
	private PGPPublicKey[] encryptionKeys;
	
	public MessageSender(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
    public byte[] compressFile(byte[] message) throws IOException {
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
	
	public void sendMessage(String messagePath, String destPath, boolean auth, boolean encrypt, int encryptAlgorithm,
			boolean zip, boolean radix, char[] pass) throws PGPException, IOException  {		
		message = null;
		
		if (auth) {
        	PGPSecretKeyRing privateKeyRing = keyGenerator.findSecretRing(privateKeyID);        	
        	if (privateKeyRing == null) {
        		JOptionPane.showMessageDialog(null, "Ne postoji kljuc za potpisivanje");
        		return;
        	}
        	
        	java.util.Iterator<PGPSecretKey> iterPriv = privateKeyRing.getSecretKeys();
        	PGPSecretKey masterKey = iterPriv.next();
//        	PGPSecretKey secretKey = iterPriv.next();
//        	PGPPrivateKey privateKey = secretKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(pass));
        	PGPPrivateKey masterPrivateKey = masterKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(pass));
        	PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(new JcaPGPContentSignerBuilder(privateKeyRing.getPublicKey().getAlgorithm(), PGPUtil.SHA1).setProvider("BC"));
        	
        	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            BCPGOutputStream helperStream = new BCPGOutputStream(byteStream);
            
            signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, masterPrivateKey);
            signatureGenerator.generateOnePassVersion(false).encode(helperStream);
            
            File file = new File(messagePath);
            PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
            OutputStream lOut = lGen.open(helperStream, PGPLiteralData.BINARY, file);
            FileInputStream fIn = new FileInputStream(file);
            int ch;
            
            while ((ch = fIn.read()) >= 0) {
                lOut.write(ch);
                signatureGenerator.update((byte)ch);
            }
            
            lGen.close();

            signatureGenerator.generate().encode(helperStream);

            message = byteStream.toByteArray();
            
            fIn.close();
            byteStream.close();
            helperStream.close();
        }
		else {
            try (InputStream inputStream = new FileInputStream(messagePath);) {
                long fileSize = new File(messagePath).length();     
                message = new byte[(int) fileSize];
                inputStream.read(message);     
            } catch (IOException ex) {
                ex.printStackTrace();
            }
		}
		
		if (zip) {
			message = compressFile(message);
		}
		
		if (encrypt) {
			
		}
		
		if (radix) {
			message = radixConversion(message);
		}
	}
	
	public void setPrivateKeyID(long keyId) {
		privateKeyID = keyId;
	}
	
	public void setEncryptionKeys(PGPPublicKey[] keys) {
		encryptionKeys = keys;
		for (PGPPublicKey key: keys) {
			System.out.println(key.getKeyID());
		}
	}
	
}
