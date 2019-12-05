package site.cnkj.utils;

import ch.ethz.ssh2.*;
import ch.ethz.ssh2.auth.AgentProxy;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.Nullable;

import java.io.*;
import java.util.Objects;

/*
 * @version 1.0 created by LXW on 2019/12/5 14:35
 */
public class SSH2Util {

    private static String DEFAULT_CHARTSET = "UTF-8";
    private static Connection connection;

    @Data
    public static class ConnectEntity{
        /**
         * the hostname of the SSH-2 server.
         */
        String hostname;
        /**
         * port on the server, normally 22.
         */
        int port = 22;
        /**
         * Allows you to set a custom "softwareversion" string as defined in RFC 4253.
         * <b>NOTE: As per the RFC, the "softwareversion" string MUST consist of printable
         * US-ASCII characters, with the exception of whitespace characters and the minus sign (-).</b>
         */
        @Nullable String softwareversion = null;
        @Nullable HTTPProxyData proxy = null;
        /**
         * A <code>String</code> holding the username.
         */
        String username;
        /**
         * 1. The password to login in.
         * 2. If the PEM structure is encrypted ("Proc-Type: 4,ENCRYPTED") then
         * you must specify a password. Otherwise, this argument will be ignored
         * and can be set to <code>null</code>.
         * 3. If the PEM file is encrypted then you must specify the password.
         * Otherwise, this argument will be ignored and can be set to <code>null</code>.
         */
        @Nullable String password = null;
        @Nullable AgentProxy au_proxy = null;
        /**
         * An <code>InteractiveCallback</code> which will be used to determine the responses to the questions asked by the server.
         */
        @Nullable InteractiveCallback cb = null;
        /**
         * An array of submethod names, see
         * draft-ietf-secsh-auth-kbdinteract-XX. May be <code>null</code>
         * to indicate an empty list.
         */
        @Nullable String[] submethods = null;
        /**
         * A <code>char[]</code> containing a DSA or RSA private key of the
         * user in OpenSSH key format (PEM, you can't miss the
         * "-----BEGIN DSA PRIVATE KEY-----" or "-----BEGIN RSA PRIVATE KEY-----"
         * tag). The char array may contain linebreaks/linefeeds.
         */
        @Nullable char[] pemPrivateKey = null;
        /**
         * A <code>File</code> object pointing to a file containing a DSA or RSA
         * private key of the user in OpenSSH key format (PEM, you can't miss the
         * "-----BEGIN DSA PRIVATE KEY-----" or "-----BEGIN RSA PRIVATE KEY-----"
         * tag).
         */
        @Nullable File pemFile = null;
    }

    private static String toString(InputStream inputStream){
        InputStream stream = new StreamGobbler(inputStream);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, DEFAULT_CHARTSET));
            while (true){
                String line = bufferedReader.readLine();
                if (StringUtils.isEmpty(line)){
                    break;
                }
                stringBuffer.append(line+"\n");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remote login to server with username and password.
     * @param connectEntity Connection entity class
     * @return Connection
     */
    public static Connection login(ConnectEntity connectEntity){
        try {
            //create connection
            if (connectEntity.getProxy() == null && StringUtils.isEmpty(connectEntity.getSoftwareversion())){
                connection = new Connection(connectEntity.getHostname(), connectEntity.getPort());
            }else if (connectEntity.getProxy() != null && StringUtils.isEmpty(connectEntity.getSoftwareversion())){
                connection = new Connection(connectEntity.getHostname(), connectEntity.getPort(), connectEntity.getProxy());
            }else if (connectEntity.getProxy() != null && StringUtils.isNotEmpty(connectEntity.getSoftwareversion())){
                connection = new Connection(connectEntity.getHostname(), connectEntity.getPort(), connectEntity.getSoftwareversion(), connectEntity.getProxy());
            }else if (connectEntity.getProxy() == null && StringUtils.isNotEmpty(connectEntity.getSoftwareversion())){
                connection = new Connection(connectEntity.getHostname(), connectEntity.getPort(), connectEntity.getSoftwareversion());
            }
            connection.connect();
            //authenticate
            boolean authenticate = false;
            if (StringUtils.isEmpty(connectEntity.getPassword()) && connectEntity.getAu_proxy() == null && connectEntity.getCb() == null && connectEntity.getSubmethods() == null && connectEntity.getPemPrivateKey() == null && connectEntity.getPemFile() == null){
                authenticate = connection.authenticateWithNone(connectEntity.getUsername());
            }else if (StringUtils.isEmpty(connectEntity.getPassword()) && connectEntity.getAu_proxy() == null && connectEntity.getCb() != null && connectEntity.getSubmethods() == null && connectEntity.getPemPrivateKey() == null && connectEntity.getPemFile() == null){
                authenticate = connection.authenticateWithKeyboardInteractive(connectEntity.getUsername(), connectEntity.getCb());
            }else if (StringUtils.isEmpty(connectEntity.getPassword()) && connectEntity.getAu_proxy() == null && connectEntity.getCb() != null && connectEntity.getSubmethods() != null && connectEntity.getPemPrivateKey() == null && connectEntity.getPemFile() == null){
                authenticate = connection.authenticateWithKeyboardInteractive(connectEntity.getUsername(), connectEntity.getSubmethods(), connectEntity.getCb());
            }else if (StringUtils.isEmpty(connectEntity.getPassword()) && connectEntity.getAu_proxy() != null && connectEntity.getCb() == null && connectEntity.getSubmethods() == null && connectEntity.getPemPrivateKey() == null && connectEntity.getPemFile() == null){
                authenticate = connection.authenticateWithAgent(connectEntity.getUsername(), connectEntity.getAu_proxy());
            }else if (StringUtils.isNotEmpty(connectEntity.getPassword()) && connectEntity.getAu_proxy() == null && connectEntity.getCb() == null && connectEntity.getSubmethods() == null && connectEntity.getPemPrivateKey() == null && connectEntity.getPemFile() == null){
                authenticate = connection.authenticateWithPassword(connectEntity.getUsername(), connectEntity.getPassword());
            }else if (connectEntity.getAu_proxy() == null && connectEntity.getCb() == null && connectEntity.getSubmethods() == null && connectEntity.getPemPrivateKey() != null && connectEntity.getPemFile() == null){
                authenticate = connection.authenticateWithPublicKey(connectEntity.getUsername(), connectEntity.getPemPrivateKey(), connectEntity.getPassword());
            }else if (connectEntity.getAu_proxy() == null && connectEntity.getCb() == null && connectEntity.getSubmethods() == null && connectEntity.getPemPrivateKey() == null && connectEntity.getPemFile() != null){
                authenticate = connection.authenticateWithPublicKey(connectEntity.getUsername(), connectEntity.getPemFile(), connectEntity.getPassword());
            }
            if (!authenticate){
                throw new RuntimeException("Failed to verify login");
            }
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Execute script
     * @param connection Connection object
     * @param cmd script
     * @return Results of the script
     */
    public static String execute(Connection connection, String cmd){
        try {
            Session session = connection.openSession();
            session.execCommand(cmd, DEFAULT_CHARTSET);
            String out = toString(session.getStdout());
            if (StringUtils.isEmpty(out)){
                throw new RuntimeException("Execution failed, return value is empty."+toString(session.getStderr()));
            }
            session.close();
            connection.close();
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Upload file to the specified path on the remote server by use scp.
     * @param connection Connection object
     * @param local Local file
     * @param remote Remote directory path
     * @return result of upload
     */
    public static Boolean upload(Connection connection, File local, String remote){
        try {
            SCPClient scpClient = new SCPClient(connection);
            SCPOutputStream scpOutputStream = scpClient.put(local.getName(), local.length(), remote, "0600");
            scpOutputStream.close();
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Batch upload files to the specified path on the specified server.
     * @param connection connection Connection object
     * @param local local directory path or file path
     * @param remote remote directory path
     * @return result of upload
     */
    public static Boolean batchUpload(Connection connection, String local, String remote){
        try {
            File localDir = new File(local);
            if (localDir.isFile()){
                return upload(connection, localDir, remote);
            }else if (localDir.isDirectory()){
                for (File file : Objects.requireNonNull(localDir.listFiles())) {
                    upload(connection, file, remote);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Download file to local from remote server.
     * @param connection Connection object
     * @param local local file path
     * @param remote remote file path
     * @return result for download
     */
    public static Boolean download(Connection connection, String local, String remote){
        FileOutputStream fileOutputStream = null;
        try {
            SCPClient scpClient = new SCPClient(connection);
            SCPInputStream scpInputStream = scpClient.get(remote);
            File localFile = new File(local);
            if (!localFile.getParentFile().exists()){
                localFile.getParentFile().mkdirs();
            }
            fileOutputStream = new FileOutputStream(localFile);
            byte[] b = new byte[1024];
            while (true){
                int read = scpInputStream.read(b);
                if (read == -1){
                    break;
                }
                fileOutputStream.write(b, 0, read);
            }
            fileOutputStream.flush();
            scpInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
