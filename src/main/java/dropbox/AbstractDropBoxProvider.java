package dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.util.Objects;

public abstract class AbstractDropBoxProvider {

    private static final String ACCESS_TOKEN = "-XPeuhXuMJAAAAAAAAAADhRZojeLzFdRm-2ILHO8wq3IydZaIv1y2Kc46csFQheP";

    protected static final String ROOT_DIRECTORY_PATH = "";

    private DbxClientV2 client;

    protected DbxClientV2 getClient() {
        if(client == null) connect();
        return client;
    }

    private void connect() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Cloud-Storage").build();
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
    }

}
