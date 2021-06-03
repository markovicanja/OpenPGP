package etf.openpgp.ma170420ddv170455d;

import java.awt.HeadlessException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.util.encoders.Base64;

public class MessageReceiver {

	private KeyGenerator keyGenerator;
	
	private byte[] message;
	
	private char[] passphrase;
	
	private String dstPath;
	
	private PGPPublicKey signerPublicKey;
	
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
    
    public byte[] radixDeconversion(byte[] data) throws Exception {
        return Base64.decode(data);
    } 
	
//    public byte[] decoder(byte[] data) throws IOException, Exception {
//    	ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
//        try {
//			data = PGPUtil.getDecoderStream(byteInputStream).readAllBytes();
//		} catch (IOException e) {
//			throw new Exception("Neuspesno");
//		}
//        byteInputStream.close();
//        return data;
//    }
    
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
	
    public void checkIntegrity(PGPPublicKeyEncryptedData encryptedData) throws HeadlessException, PGPException, IOException {
		if (encryptedData != null) {
			if (encryptedData.isIntegrityProtected()) {
				if (encryptedData.verify()) {
					System.out.println("Provera integriteta uspesna!");
				}
				else {
					System.out.println("Provera integriteta neuspesna!");
				}
			}
		}
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
        
        checkIntegrity(encryptedData);
        
        return decryptedBytes;
    }
    
    public boolean isSigned(byte[] data) throws IOException, PGPException {
    	JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
    	Object o = null;
    	try {
    		o = objectFactory.nextObject();
    	} catch(IOException e) {
    		return false;
    	}    	
    	if (o instanceof PGPOnePassSignatureList) {
    		PGPOnePassSignatureList signatureList = (PGPOnePassSignatureList) o;
    		PGPOnePassSignature signature = signatureList.get(0);
			long keyId = signature.getKeyID();			
			PGPPublicKeyRing ring = keyGenerator.findPublicRing(keyId);
		    signerPublicKey = ring.getPublicKey(keyId);
		   
		    signature.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), signerPublicKey);
		    
		    PGPLiteralData literalData = (PGPLiteralData)objectFactory.nextObject();
		    InputStream is = literalData.getInputStream();
	    	byte[] allData = is.readAllBytes();
	    	for(byte i : allData) signature.update(i);
	    	PGPSignatureList sigList = (PGPSignatureList)objectFactory.nextObject();
	    	PGPSignature sig = sigList.get(0);
	    	if (signature.verify(sig)) {
	    		String author = new String(signerPublicKey.getRawUserIDs().next(), StandardCharsets.UTF_8);
	    		System.out.println("Uspesna provera potpisa! Autor: " + author);
	    		return true;
	    	}
	    	else {
	    		System.out.println("Neuspesna provera potpisa...");
	    		return false;
	    	}
    	}
    	return false;
    }
    
    public byte[] unsignedMessage(byte[] data) throws IOException {
        JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
        objectFactory.nextObject();
        PGPLiteralData literalData = (PGPLiteralData) objectFactory.nextObject();
        return literalData.getInputStream().readAllBytes();
    }
	
	public void receiveMessage(String srcPath, String dstPath) throws IOException, PGPException {
		message = Files.readAllBytes(Paths.get(srcPath));
		
		this.dstPath = dstPath;
		
		try {
			message = radixDeconversion(message);
		} catch (Exception e) {
			message = Files.readAllBytes(Paths.get(srcPath));
		}
		
		if (isEncrypted(message)) {
			PasswordGUI passwordGUI = new PasswordGUI(this);
			passwordGUI.setVisible(true);
			return;
		}	
 
		if (isCompressed(message)) {			
			message = decompressData(message);
		} 
		
		if (isSigned(message)) {
			message = unsignedMessage(message);
		}

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
		} 
		
		if (isSigned(message)) {
			message = unsignedMessage(message);
		}

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
}
