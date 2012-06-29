package ch.baws.projectneo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.frameGenerator.PacketGenerator;
import ch.baws.projectneo.minions.NotImplementedException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * class Bluetooth utils
 * provides basic bluetooth functions
 **/

public class BluetoothUtils {
	private static final String TAG = "BN_BTUTILS";
	private static final boolean D = false;
	private static final boolean E = false;
	
	private boolean FLAG_connected = false;
	
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket btSocket = null;
	
	private OutputStream out;
	private InputStream in;
	
	
	// Well known SPP UUID (will *probably* map to
	// RFCOMM channel 1 (default) if not in use);
	// see comments in onResume().
	private static final UUID MY_UUID = 
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// ==> hardcode your server's MAC address here <==
	private static String address = "00:07:80:85:8B:6E";

	/**
	 * method Available
	 * initializes the BT connection
	 */
	public BluetoothUtils(){
        if (D) Log.d(TAG, "+++ Init +++");
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (D) Log.d(TAG, " Inited...");
	}
	
	public static boolean active()
	{
        if (D)	Log.e(TAG, "+++ Active +++");
		return BluetoothAdapter.getDefaultAdapter().isEnabled();
	}
	
	/**
	 * method Test
	 * tests if BT connection is working
	 * @return
	 */
	public boolean test()
	{
		throw new NotImplementedException();
	}
	
	/**
	 * method connect
	 * @param BluetoothAdapter
	 * @param BluetoothSocket
	 * @return void
	 **/
	public boolean connect() 
	{
		if (D) Log.d(TAG, "Bluetooth connecting");
		boolean error = false;
		
		if(!mBluetoothAdapter.isEnabled()){
			return true;
		}
		
		if(E) return true;
		
		
   		// When this returns, it will 'know' about the server,
   		// via it's MAC address.
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
   		// We need two things before we can successfully connect
   		// (authentication issues aside): a MAC address, which we
   		// already have, and an RFCOMM channel.
   		// Because RFCOMM channels (aka ports) are limited in
   		// number, Android doesn't allow you to use them directly;
   		// instead you request a RFCOMM mapping based on a service
   		// ID. In our case, we will use the well-known SPP Service
   		// ID. This ID is in UUID (GUID to you Microsofties)
   		// format. Given the UUID, Android will handle the
   		// mapping for you. Generally, this will return RFCOMM 1,
   		// but not always; it depends what other BlueTooth services
   		// are in use on your Android device.
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			Log.e(TAG, "ON RESUME: Socket creation failed.", e);
			error = true;
		}

   		// Discovery may be going on, e.g., if you're running a
   		// 'scan for devices' search from your handset's Bluetooth
   		// settings, so we call cancelDiscovery(). It doesn't hurt
   		// to call it, but it might hurt not to... discovery is a
   		// heavyweight process; you don't want it in progress when
   		// a connection attempt is made.
   		
		mBluetoothAdapter.cancelDiscovery();

   		// Blocking connect, for a simple client nothing else can
   		// happen until a successful connection is made, so we
   		// don't care if it blocks.
		if(btSocket==null){
			error = true;
			return error;
		}
   		try {
   			btSocket.connect();
   			FLAG_connected = true;
   			Log.d(TAG, "BT connection established, data transfer link open.");
   		} catch (IOException e) {
   			try {
   				btSocket.close();
   			} catch (IOException e2) {
   				Log.e(TAG, 
   					"ON RESUME: Unable to close socket during connection failure", e2);
   			}
   			error = true;
   		}		
   		
   		//get streams
   		try {
   			in = btSocket.getInputStream();
			out = btSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "ON RESUME: Output stream creation failed.");
			error = true;
		}
   		
		return error;
	}
	/**
	 * method Send
	 * @param BluetoothAdapter
	 * @param BluetoothSocket
	 * @return void
	 **/
	public boolean send(int[][] colorArray) 
	{
		boolean error = false;
		if(E) return false; //Emulator mode, do nothing
		
   		// Create a data stream so we can talk to server.
   		if (D){
   			Log.d(TAG, "+ ABOUT TO SAY SOMETHING TO SERVER +");
   			Log.d(TAG, "colorArray[0][0]: "+ colorArray[0][0]);
   		}
   		
   		if(btSocket ==null || out==null){
   			Log.e(TAG, "ERROR: Socket or Stream is NULL.");
   			error = true;
   			return error;
   		}

		Frame frame = new Frame(colorArray);
   		byte[] packet = PacketGenerator.pack(frame);

    	try {
    		out.write(packet);
    	} catch (IOException e) {
    		Log.e(TAG, "ERROR: Exception during write. IOException.", e);
    		error = true;
    	}
    	return error;
	}
	
	public boolean isConnected(){
		//return this.btSocket.isConnected();  //Why doesn't this work???
		return this.FLAG_connected;
	}
	
	public byte[] read(){
		if(D) Log.d(TAG,"Bluetooth read");
		int read_bytes=0;
		byte[] buffer = new byte[1024];
		try {
			read_bytes = in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "ERROR: Exception during read. IOException.", e);
		}
		if(D) Log.d(TAG,"read " + read_bytes + " bytes from BluetoothSocket");
		return buffer;
	}
	
	
	/**
	 * method Close
	 * shut down BT connection
	 * @return 
	 * @return
	 */
	public void close() //using this method gives FC
	{
		if(!mBluetoothAdapter.isEnabled()){
			if(D) Log.d(TAG, "no bluetoothadapter to close?");
		}
		
   		try	{
   			if(btSocket!=null) btSocket.close();
   			this.FLAG_connected = false;
   			if(D) Log.d(TAG, "closed socket");
   		} catch (IOException e2) {
   			Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
		}
	}

	

		
}
