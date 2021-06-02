package etf.openpgp.ma170420ddv170455d;

public class MessageReceiver {
/*
 * 

    public static byte[] unzip(byte[] data) throws Exception {
        JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
        Object o = objectFactory.nextObject();
        if(!(o instanceof PGPCompressedData))
            throw new Exception("Unable to unzip data");
        PGPCompressedData cdata = (PGPCompressedData) o;

        return cdata.getDataStream().readAllBytes();
    }

    public static byte[] decodeArmoredStream(byte[] data) throws IOException {
        return PGPUtil.getDecoderStream(new ByteArrayInputStream(data)).readAllBytes();
    }

    public static byte[] encodeBase64(byte[] data)
    {
        return Base64.encode(data);
    }

    public static byte[] decodeBase64(byte[] data)
    {
        return Base64.decode(data);
    }

    public static byte[] decrypt(byte[] data, PGPPrivateKey privateKey) throws Exception {
        JcaPGPObjectFactory objectFactory = new JcaPGPObjectFactory(data);
        Object o = objectFactory.nextObject();

        if(o instanceof PGPEncryptedDataList)
        {
            PGPEncryptedDataList edl = (PGPEncryptedDataList) o;
            Iterator<PGPEncryptedData> encryptedDataObjects = edl.getEncryptedDataObjects();
            PGPPublicKeyEncryptedData encryptedData = (PGPPublicKeyEncryptedData) encryptedDataObjects.next();
            PublicKeyDataDecryptorFactory dataDecryptorFactory =
                    new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(privateKey);
            byte[] decryptedBytes = encryptedData.getDataStream(dataDecryptorFactory).readAllBytes();
            return decryptedBytes;
        }
        throw new Exception("Provided data is not encrypted");
    }

    public static boolean verifySignature(byte[] pgpSignedData, PGPPublicKey verifyingKey) throws IOException, PGPException {
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);

        PGPOnePassSignatureList opl = (PGPOnePassSignatureList)pgpFact.nextObject();
        PGPOnePassSignature ops = opl.get(0);

        PGPLiteralData literalData = (PGPLiteralData)pgpFact.nextObject();
        InputStream is = literalData.getInputStream();

        ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), verifyingKey);

        byte[] data = is.readAllBytes();
        for(byte i : data)
            ops.update(i);

        PGPSignatureList sigList = (PGPSignatureList)pgpFact.nextObject();
        PGPSignature sig = sigList.get(0);

        return ops.verify(sig);
    }

    public static byte[] readSignedMessage(byte[] pgpSignedData) throws IOException {
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);
        pgpFact.nextObject();
        PGPLiteralData literalData = (PGPLiteralData) pgpFact.nextObject();

        return literalData.getInputStream().readAllBytes();
    }
 * */
}
