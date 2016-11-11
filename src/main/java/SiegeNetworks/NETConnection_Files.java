package SiegeNetworks;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NETConnection_Files extends NETConnection
{
    protected BufferedInputStream inputFileStreamBuffered = null;
    protected BufferedOutputStream outputFileStreamBuffered = null;

    public File dcm_File=null;
    protected FileInputStream inputFileStream = null;
    protected FileOutputStream outputFileStream = null;

    protected byte[] byteEArr = new byte[2048];

    public NETConnection_Files(){}

    public NETConnection_Files(int Port, String address_IP) throws java.io.IOException
    {
        super(Port,address_IP);
    }

    public NETConnection_Files(int Port) throws java.io.IOException
    {
        super(Port);
    }

    @Override
    public void init(int Port, String address_IP)
    {
        System.out.println("NETConnection_Files: Connecting...");
        try {
            dcm_Socket = new Socket(address_IP, Port);
            System.out.println("NETConnection_Files: Connected to: "+address_IP+":"+Port);
            outputBufferedStream = new BufferedOutputStream(dcm_Socket.getOutputStream());
            inputBufferedStream = new BufferedInputStream(dcm_Socket.getInputStream());
            inputStreamReader = new BufferedReader(new InputStreamReader(dcm_Socket.getInputStream()));
            outputStreamReader= new BufferedWriter(new OutputStreamWriter(outputBufferedStream));
        }
        catch (IOException e)
        {
            try
            {
                throw new IOException("NETConnection_Files: [ERROR] Unable to connect to: "+address_IP+":"+Port);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void init(int Port)
    {
        System.out.println("NETConnection_Files: Setting up a server...");
        try
        {
            dcm_serverSocket = new ServerSocket(Port);
            System.out.println("NETConnection_Files: Server at port "+Port+" has been initialized.");
        }
        catch (IOException e)
        {
            try
            {
                throw e;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void recvConnection() {
        try {
            System.out.println("NETConnection_Files: Waiting for an incoming connection...");
            dcm_Socket=dcm_serverSocket.accept();
            System.out.println("NETConnection_Files: Connection accepted. Initializing streams...");
            outputBufferedStream = new BufferedOutputStream(dcm_Socket.getOutputStream());
            inputBufferedStream = new BufferedInputStream(dcm_Socket.getInputStream());
            inputStreamReader = new BufferedReader(new InputStreamReader(inputBufferedStream));
            outputStreamReader= new BufferedWriter(new OutputStreamWriter(outputBufferedStream));
            System.out.println("NETConnection_Files: Streams up. Waiting for commands...");
            //exec(String command, String[] envp, File dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSendFile(String sendFile) throws java.io.IOException
    {
        try{
            dcm_File=new File(sendFile);
            inputFileStream = new FileInputStream(dcm_File);
            inputFileStreamBuffered = new BufferedInputStream(inputFileStream);
            inputFileStreamBuffered.close();
            inputFileStream.close();
        }
        catch (IOException e) {
            throw new IOException("NETConnection_Files: [ERROR] Unable to close/create send file streams, or file does not exist.");
        }
    }

    public void createRecvFile(String recvFile) throws java.io.IOException
    {
        try{
            inputFileStream = new FileInputStream(recvFile);
            inputFileStreamBuffered = new BufferedInputStream(inputFileStream);


            outputFileStream.close();
            inputFileStreamBuffered.close();


        }
        catch (IOException e) {
            throw new IOException("NETConnection_Files: [ERROR] Unable to close/create send file streams");
        }
    }

    //SendFile
    public void sendFile(String sendFile) throws java.io.IOException
    {
        loadSendFile(sendFile);
        sendFile();
    }

    public void sendFile() throws java.io.IOException
    {
        System.out.println("NETConnection_Files: Server have received the file header data. Sending file itself...");
        inputFileStreamBuffered.read(byteEArr, 0, byteArr.length);
        System.out.println("NETConnection_Files: Sending file data...");
        outputBufferedStream.write(byteEArr, 0, byteArr.length);
        outputBufferedStream.flush();
        System.out.println("NETConnection_Files: File sent.");
    }

    //RecvFile
    public void recvFile(String fileName, int st_openOnDownload) throws java.io.IOException
    {
        createRecvFile(fileName);
        recvFile(st_openOnDownload);
    }

    public void recvFile(int st_openOnDownload) throws java.io.IOException
    {
        inputFileStream=new FileInputStream(dcm_File);
        inputFileStreamBuffered = new BufferedInputStream(inputFileStream);

        inputBufferedStream = new BufferedInputStream(dcm_Socket.getInputStream());
        while((buffByteArrSize= inputBufferedStream.read(byteEArr)) >0){
            outputFileStream.write(byteEArr);
        }
        inputFileStream.close();
        inputFileStreamBuffered.close();
    }

    @Override
    public void deInit() throws java.io.IOException
    {
        super.deInit();
        try {
            if (inputFileStream != null) inputFileStream.close();
            if (outputFileStream != null) outputFileStream.close();
            if (inputFileStreamBuffered != null) inputFileStreamBuffered.close();
            if (outputFileStreamBuffered != null) outputFileStreamBuffered.close();
        }
        catch(IOException e)
        {
            throw new IOException("NETConnection_Files: [ERROR] Unable to close file data streams at deInit()");
        }
    }

}

    /*

            int count;
            while ((count = inputFileStreamBuffered.read(byteArr)) > 0)
            {
                outputBufferedStream.write(byteArr, 0, count);
            }

            outputFileStreamBuffered = new BufferedOutputStream(outputBufferedStream);
            bytesRead = inputBufferedStream.read(byteArr, 0, byteArr.length);
            currentPart = bytesRead;

            do{
                bytesRead = inputBufferedStream.read(byteArr, currentPart, (byteArr.length - currentPart));
                if (bytesRead >= 0) currentPart = currentPart + bytesRead;
            } while (bytesRead > -1);


*/
