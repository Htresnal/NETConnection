package SiegeNetworks;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NETConnection_Files extends NETConnection
{
    protected OutputStream outputStream = null;

    public File dcm_File=null;
    protected FileInputStream inputFileStream = null;
    protected FileOutputStream outputFileStream = null;
    protected BufferedInputStream inputFileBufferedStream = null;
    protected BufferedOutputStream outputFileBufferedStream = null;

    protected byte[] byteEArr = new byte[8196];

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
    public void init(int Port, String address_IP) throws IOException {
        super.init(Port, address_IP);
    }

    @Override
    public void init(int Port) throws IOException {
        super.init(Port);
    }

    @Override
    public void recvConnection()
    {
        super.recvConnection();
    }

    public void loadSendFile(String sendFile) throws java.io.IOException
    {
        try{
            dcm_File=new File(sendFile);
            inputFileStream=new FileInputStream(dcm_File);
            inputFileBufferedStream=new BufferedInputStream(inputFileStream);
            outputFileBufferedStream=new BufferedOutputStream(dcm_Socket.getOutputStream());
        }
        catch (IOException e)
        {
            throw e;
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
        System.out.println("NETConnection_Files: Sending file data...");
        /*
        while ((buffByteArrSize = inputFileBufferedStream.read(byteEArr, 0, buffByteArrSize)) > 0)
        {
            outputFileBufferedStream.write(byteEArr, 0, buffByteArrSize);
            System.out.println(byteEArr);
        }
        */
        while ((inputFileBufferedStream.read(byteEArr)) > 0)
        {
            outputFileBufferedStream.write(byteEArr);
            System.out.println(Integer.toString(byteEArr.length));
        }
        System.out.println("NETConnection_Files: File sent.");



        /*
        int count;
        while ((count = inputStream.read(byteArr)) > 0)
        {
            outputBufferedStream.write(byteArr, 0, count);
        }

        outputStream = new BufferedOutputStream(outputBufferedStream);
        bytesRead = inputBufferedStream.read(byteArr, 0, byteArr.length);
        currentPart = bytesRead;

        do{
            bytesRead = inputBufferedStream.read(byteArr, currentPart, (byteArr.length - currentPart));
            if (bytesRead >= 0) currentPart = currentPart + bytesRead;
        } while (bytesRead > -1);
        */
    }

    //RecvFile
    public void recvFile(String fileName, boolean st_openOnDownload) throws java.io.IOException
    {
        try{
            dcm_File=new File(fileName);
            outputFileStream=new FileOutputStream(dcm_File);

            System.out.println("[RECVFILE]: Initialized.. Starting the reading cycle...");

            while ((buffByteArrSize = inputBufferedStream.read(byteEArr)) > 0)
            {
                outputFileStream.write(byteEArr,0,buffByteArrSize);
                System.out.println(buffByteArrSize);
            }

            System.out.println("[RECVFILE]: Reading cycle finished.");

            outputFileStream.close();

            if (st_openOnDownload) {
                Desktop dt = Desktop.getDesktop();
                dt.open(dcm_File);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    @Override
    public void deInit() throws java.io.IOException
    {
        super.deInit();
        try {
            if (inputFileStream != null) inputFileStream.close();
            if (outputFileStream != null) outputFileStream.close();
            if (inputFileBufferedStream != null) inputFileBufferedStream.close();
            if (outputFileBufferedStream != null) outputFileBufferedStream.close();
        }
        catch(IOException e)
        {
            throw e;
        }
    }

}