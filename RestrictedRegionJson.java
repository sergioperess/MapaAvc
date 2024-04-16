import java.security.KeyPair;

public class RestrictedRegionJson extends Region {

    private Region mainRegion;
    private byte[] encryptedData;
    private String serializedData;

    public RestrictedRegionJson(Region mainRegion) {
        this.mainRegion = mainRegion;
    }

    public Region getMainRegion() {
        return mainRegion;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public String getSerializedData() {
        return serializedData;
    }

    public void encryptAndSerializeData(KeyPair keyPair) {
        EncryptionThread encryptionThread = new EncryptionThread();
        JsonSerializationThread jsonSerializationThread = new JsonSerializationThread();
        encryptionThread.setRegion(this);
        jsonSerializationThread.setRegion(this);
        encryptionThread.start();
        try {
            encryptionThread.join(); // Aguardar a conclusão da criptografia
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jsonSerializationThread.start();
        try {
            jsonSerializationThread.join(); // Aguardar a conclusão da serialização JSON
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class EncryptionThread extends Thread {
        private RestrictedRegionJson restrictedRegion;

        public void setRegion(RestrictedRegionJson restrictedRegion) {
            this.restrictedRegion = restrictedRegion;
        }

        @Override
        public void run() {
            try {
                // Criptografar os dados da RestrictedRegion
                byte[] encryptedData = encryptRSA(restrictedRegion.toString().getBytes(), keyPair.getPrivate());
                restrictedRegion.encryptedData = encryptedData;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class JsonSerializationThread extends Thread {
        private RestrictedRegionJson restrictedRegion;

        public void setRegion(RestrictedRegionJson restrictedRegion) {
            this.restrictedRegion = restrictedRegion;
        }

        @Override
        public void run() {
            try {
                // Serializar a RestrictedRegion para formato JSON
                String json = restrictedRegion.toJson();
                restrictedRegion.serializedData = json;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
