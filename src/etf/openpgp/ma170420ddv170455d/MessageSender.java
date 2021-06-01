package etf.openpgp.ma170420ddv170455d;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
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
	
//    public byte[] compressFile(byte[] message) throws IOException {
//        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
//        OutputStream compressedOutputStream = comData.open(bOut);
//        compressedOutputStream.write(message);
//    	compressedOutputStream.close();    	
//    	message = bOut.toByteArray();
//    	bOut.close();
//        return message;
//    }
//    
//    public byte[] radixConversion(byte[] message) throws IOException {
//    	ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
//        ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(byteOutputStream);
//        armoredOutputStream.write(message);
//        armoredOutputStream.close();
//        message = byteOutputStream.toByteArray();        
//        byteOutputStream.close();
//        return message;
//    }
	
	public void sendMessage(String messagePath, String destPath, boolean auth, boolean encrypt, int encryptAlgorithm,
			boolean zip, boolean radix, char[] pass) throws PGPException, IOException, NoSuchProviderException  {	
		
		FileOutputStream destFile = new FileOutputStream(destPath);
		File src = new File(messagePath);
    	
    	PGPPublicKey publicKey = encryptionKeys[0]; // NAPRAVI ZA SVE PUBLIC KLJUCEVE
    	
    	PGPSecretKey secretKey = null;    	
    	PGPSecretKeyRing ring = keyGenerator.findSecretRing(privateKeyID);
		Iterator<PGPSecretKey> iterKey = ring.getSecretKeys();
		while (iterKey.hasNext()) {
			PGPSecretKey key = iterKey.next();
			if (key.getKeyID() == privateKeyID) {
				secretKey = key;
				break;
			}
		}
    	
    	encryptFile(destFile, src, publicKey, secretKey, radix, pass, auth, encrypt, zip, encryptAlgorithm);
    	destFile.close();		
	}
	
	public void setPrivateKeyID(long keyId) {
		privateKeyID = keyId;
	}
	
	public void setEncryptionKeys(PGPPublicKey[] keys) {
		encryptionKeys = keys;
	}
    
    public void encryptFile(OutputStream out, File file, PGPPublicKey encKey, PGPSecretKey pgpSec, 
    		boolean radix, char[] pass, boolean auth, boolean encrypt, boolean zip, int encryptAlgorithm) 
    				throws IOException, NoSuchProviderException {
        if (radix)  out = new ArmoredOutputStream(out);

        try {
        	OutputStream encryptedOut = null;
        	PGPEncryptedDataGenerator encGen = null;        	
        	
        	if (auth) {
        		// Provera integriteta
	            encGen = new PGPEncryptedDataGenerator(
	                    new JcePGPDataEncryptorBuilder(encryptAlgorithm)
	                    .setWithIntegrityPacket(true)
	                    .setSecureRandom(new SecureRandom())
	                    .setProvider("BC"));
	            encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"));
	            encryptedOut = encGen.open(out, new byte[1 << 16]);
        	}
        	else encryptedOut = out;
        	
            int compressedDataInt = PGPCompressedData.UNCOMPRESSED;
            PGPCompressedDataGenerator comData = null;
            OutputStream compressedData = null;
            if (zip) compressedDataInt = PGPCompressedData.ZIP;
            
            comData = new PGPCompressedDataGenerator(compressedDataInt);
        	compressedData = comData.open(encryptedOut);

            //OutputStream compressedData = encryptedOut;
            PGPSignatureGenerator sGen = null;
            if (auth) {
	            PGPPrivateKey pgpPrivKey = pgpSec.extractPrivateKey(
	                    new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(pass));
	            sGen = new PGPSignatureGenerator(new JcaPGPContentSignerBuilder(
	                    pgpSec.getPublicKey().getAlgorithm(), PGPUtil.SHA1).setProvider("BC"));
	            sGen.init(PGPSignature.BINARY_DOCUMENT, pgpPrivKey);
	            Iterator<String> it = pgpSec.getPublicKey().getUserIDs();
	            if (it.hasNext()) {
	                PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
	                spGen.setSignerUserID(false, (String) it.next());
	                sGen.setHashedSubpackets(spGen.generate());
	            }
	            //BCPGOutputStream bOut = new BCPGOutputStream(compressedData);
	            sGen.generateOnePassVersion(false).encode(compressedData); // bOut
            }
           
            PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
            OutputStream lOut = lGen.open(compressedData, PGPLiteralData.BINARY, file.getName(), new Date(),
                                          new byte[1 << 16]); //bOut
            FileInputStream fIn = new FileInputStream(file);
            int ch;

            while ((ch = fIn.read()) >= 0) {
                lOut.write(ch);
                if(auth)
                sGen.update((byte) ch);
            }

          
            fIn.close();
            lOut.close();
            lGen.close();
            if(auth) sGen.generate().encode(compressedData);

            //bOut.close();
            
            compressedData.close();
            if(comData != null)
            comData.close();
            encryptedOut.close();
            if(encGen != null)
            encGen.close();

            if (radix) out.close();
        } catch (PGPException e) {
            System.out.println(e);
        }  
      
    
    }
	
}
