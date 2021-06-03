package etf.openpgp.ma170420ddv170455d;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.lang.Exception;

import javax.swing.JOptionPane;

import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;

public class MessageReceiver {

	private KeyGenerator keyGenerator;
	
	private byte[] message;
	
	private char[] passphrase;
	
	private String dstPath;
	
	public MessageReceiver(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	public byte[] decompressData(byte[] data) throws IOException, PGPException {
		JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
        Object o = objectFactory.nextObject();
        PGPCompressedData cdata = (PGPCompressedData) o;
        return cdata.getDataStream().readAllBytes();
    }
	
	public boolean isCompressed(byte[] data) {
		PGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
		Object o = null;
		try {
			o = objectFactory.nextObject();
		} catch (IOException e) {
			return false;
		}
		if (o instanceof PGPCompressedData) {
			return true;
		}
		return false;
	}
	
    public byte[] radixDeconversion(byte[] data) throws IOException, Exception {
    	ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        try {
			data = PGPUtil.getDecoderStream(byteInputStream).readAllBytes();
		} catch (IOException e) {
			throw new Exception("Radix neuspesan");
		}
        byteInputStream.close();
        return data;
    }
    
    public boolean isEncrypted(byte[] data) {
    	JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
        Object o;
		try {
			o = objectFactory.nextObject();
		} catch (IOException e) {
			return false;
		}
        if (o instanceof PGPEncryptedDataList) 
        	return true;
        return false;
    }
	
    public byte[] decrypt(byte[] data, PGPPrivateKey privateKey) throws Exception {
        JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
        PGPEncryptedDataList edl = (PGPEncryptedDataList)(objectFactory.nextObject());
        Iterator<PGPEncryptedData> encryptedDataObjects = edl.getEncryptedDataObjects();
        PGPPublicKeyEncryptedData encryptedData = (PGPPublicKeyEncryptedData) encryptedDataObjects.next();
        PublicKeyDataDecryptorFactory dataDecryptorFactory =
                new JcePublicKeyDataDecryptorFactoryBuilder()
                .setProvider("BC")
                .build(privateKey);
        InputStream is = encryptedData.getDataStream(dataDecryptorFactory);
        byte[] decryptedBytes = is.readAllBytes();
        return decryptedBytes;
    }
    
    public boolean signed(byte[] pgpSignedData, PGPPublicKey verifyingKey) throws IOException, PGPException {
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);

        PGPOnePassSignatureList opl = (PGPOnePassSignatureList)pgpFact.nextObject();
        PGPOnePassSignature ops = opl.get(0);

        PGPLiteralData literalData = (PGPLiteralData)pgpFact.nextObject();
        InputStream is = literalData.getInputStream();

        ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), verifyingKey);

        byte[] data = is.readAllBytes();
        for(byte i : data) ops.update(i);

        PGPSignatureList sigList = (PGPSignatureList)pgpFact.nextObject();
        PGPSignature sig = sigList.get(0);

        return ops.verify(sig);
    }
    
    public byte[] readSignedMessage(byte[] pgpSignedData) throws IOException {
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);
        pgpFact.nextObject();
        PGPLiteralData literalData = (PGPLiteralData) pgpFact.nextObject();

        return literalData.getInputStream().readAllBytes();
    }
	
	public void receiveMessage(String srcPath, String dstPath) throws IOException, PGPException {
		message = Files.readAllBytes(Paths.get(srcPath));
		byte[] data; 
		
		this.dstPath = dstPath;
	
		// Treba nekako da se dohvati publicKey
		PGPPublicKey publicKey = null;
		
		try {
			data = radixDeconversion(message);
			message = data;
			System.out.println("Radix");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Radix64 dekonverzija nije uspela!");
			return;
		}
		
		if (isEncrypted(message)) {
			System.out.println("Enkripcija");
			PasswordGUI passwordGUI = new PasswordGUI(this);
			passwordGUI.setVisible(true);
			return;
		}

		// OVO SVE TREBA DA SE ISKOPIRA U continueSending		
 
		if (isCompressed(message)) {			
			message = decompressData(message);
			System.out.println("Dekompresija");
		} 
		
//		if (signed(message, publicKey)) {
//			message = readSignedMessage(message);
//		}

		try (FileOutputStream fos = new FileOutputStream(dstPath)) {
 		   fos.write(message);
		}	
 	
		JOptionPane.showMessageDialog(null, "Poruka je primljena!");
	}
	
	public void continueReceiving() throws Exception {	
		PGPObjectFactory objectFactory = new JcaPGPObjectFactory(message);
		PGPEncryptedDataList enc = (PGPEncryptedDataList) (objectFactory.nextObject());
		java.util.Iterator<PGPEncryptedData> it = enc.getEncryptedDataObjects();
	    PGPPublicKeyEncryptedData pbe = (PGPPublicKeyEncryptedData) it.next();
        PGPSecretKeyRing keyRing = keyGenerator.findSecretRing(pbe.getKeyID());
        
        PGPPrivateKey privateKey = null;        
        try {
			java.util.Iterator<PGPSecretKey> iterPriv = keyRing.getSecretKeys();
        	PGPSecretKey secretKey = iterPriv.next();
        	secretKey = iterPriv.next();
			privateKey = secretKey.extractPrivateKey(
					new JcePBESecretKeyDecryptorBuilder()
					.setProvider("BC")
					.build(passphrase));
		} catch (PGPException e2) {
			JOptionPane.showMessageDialog(null, "Pogresna lozinka za privatni kljuc!");
			return;
		}
         
		message = decrypt(message, privateKey);
		
		if (isCompressed(message)) {			
			message = decompressData(message);
			System.out.println("Dekompresija");
		} 
		
//		if (signed(message, publicKey)) {
//			message = readSignedMessage(message);
//		}
		
		try (FileOutputStream fos = new FileOutputStream(dstPath)) {
		   fos.write(message);
		}	
	
		JOptionPane.showMessageDialog(null, "Poruka je primljena!");
	}
	
	public void setPassphrase(char[] pass) {
		this.passphrase = pass;
		try {
			continueReceiving();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*
 * Ne znam jel trebaju ove metode?
 *  
 *  public static byte[] encodeBase64(byte[] data)
    {
        return Base64.encode(data);
    }

    public static byte[] decodeBase64(byte[] data)
    {
        return Base64.decode(data);
    } 
*/
}
