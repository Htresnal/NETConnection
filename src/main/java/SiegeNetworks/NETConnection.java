package SiegeNetworks;

//Initializer(Port,IP) - If you set up two parameters, you'll get a client version of the NETConnection.
//Initializer(Port) - If you set up just a single parameter, you'll get a server version.

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class NETConnection
{
    protected enum ConnStatus {
        NONE, CLIENT, SERVER
    }

    protected String dcm_address_IP;
    protected int dcm_Port;
    protected ConnStatus dcm_ConnType = ConnStatus.NONE;
    protected Socket dcm_Socket = null;
    protected ServerSocket dcm_serverSocket=null;

    protected BufferedInputStream inputBufferedStream = null;
    protected BufferedOutputStream outputBufferedStream = null;
    protected BufferedReader inputStreamReader = null;
    protected BufferedWriter outputStreamReader = null;


    protected int buffByteArrSize=0;
    protected char[] byteArr = new char[512];

    public NETConnection(){}

    public NETConnection(int Port, String address_IP) throws java.io.IOException
    {
        dcm_Port=Port;
        dcm_address_IP=address_IP;
        dcm_ConnType = ConnStatus.CLIENT;
    }

    public NETConnection(int Port) throws java.io.IOException
    {
        dcm_Port=Port;
        dcm_ConnType = ConnStatus.SERVER;
    }

    public void init(int Port, String address_IP) throws IOException {
        try {
            dcm_Socket = new Socket(address_IP, Port);
            outputBufferedStream = new BufferedOutputStream(dcm_Socket.getOutputStream());
            inputBufferedStream = new BufferedInputStream (dcm_Socket.getInputStream());
            inputStreamReader = new BufferedReader(new InputStreamReader(inputBufferedStream));
            outputStreamReader= new BufferedWriter(new OutputStreamWriter(outputBufferedStream));
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    public void init(int Port) throws IOException {
        try
        {
            dcm_serverSocket = new ServerSocket(Port);
            //Server-side stream loading was moved to recvConnection
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    public String connectionInfo()
    {
        return String.valueOf(dcm_Socket.getInetAddress());
    }

    public void recvConnection()
    {
        try {
            dcm_Socket=dcm_serverSocket.accept();
            outputBufferedStream = new BufferedOutputStream(dcm_Socket.getOutputStream());
            inputBufferedStream = new BufferedInputStream(dcm_Socket.getInputStream());
            inputStreamReader = new BufferedReader(new InputStreamReader(inputBufferedStream));
            outputStreamReader= new BufferedWriter(new OutputStreamWriter(outputBufferedStream));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendSentence(String inputString) throws IOException
    {
        try
        {
            outputStreamReader.write(inputString);
            outputStreamReader.flush();
        }
        catch (IOException e)
        {
            throw e;
        }
    }

    public String getSentence() throws IOException
    {
        StringBuffer sb = new StringBuffer();
        char[] tmpBuffChar;
        do {
            tmpBuffChar = new char[] { 1024 };
            inputStreamReader.read(tmpBuffChar);
            sb.append(tmpBuffChar);
        } while (inputStreamReader.ready());

        return sb.toString();
    }

    public void deInit() throws java.io.IOException
    {
        try {
            if (dcm_Socket != null) dcm_Socket.close();
            if (dcm_serverSocket != null) dcm_serverSocket.close();
            if (inputBufferedStream != null) inputBufferedStream.close();
            if (outputBufferedStream != null) outputBufferedStream.close();
            if (inputStreamReader != null) inputStreamReader.close();
            if (outputStreamReader != null) outputStreamReader.close();
        }
        catch (IOException e) {
            throw e;
        }
    }
}