package cn.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * date: 2020/5/5 20:53
 * author: zengfansheng
 */
class SocketConnection {
    private BluetoothSocket socket;
    private final BluetoothDevice device;
    private OutputStream outStream;
    private InputStream inputStream;
    private final ConnectionImpl connection;

    @SuppressLint("MissingPermission")
    SocketConnection(final ConnectionImpl connection,
                     final BTManager btManager,
                     final BluetoothDevice device,
                     final UUID uuid,
                     final ConnectCallback callback) {
        this.device = device;
        this.connection = connection;
        BluetoothSocket tmp;
        try {
            connection.changeState(Connection.STATE_CONNECTING, false);
            tmp = device.createRfcommSocketToServiceRecord(uuid == null ? Connection.SPP_UUID : uuid);
        } catch (IOException e) {
            try {
                Method method = device.getClass()
                        .getMethod("createRfcommSocket",
                                new Class[]{int.class});
                tmp = (BluetoothSocket) method.invoke(device, 1);
            } catch (Throwable t) {
                onConnectFail(connection,
                        callback,
                        "Connect failed: Socket's create() method failed", e);
                return;
            }
        }
        socket = tmp;
        btManager.getExecutorService().execute(() -> {
            InputStream tmpInput;
            OutputStream tmpOut;
            try {
                //停止搜索
                btManager.stopDiscovery();
                socket.connect();
                tmpInput = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                if (!connection.isReleased()) {
                    onConnectFail(connection, callback,
                            "Connect failed: " + e.getMessage(), e);
                }
                return;
            }
            outStream = tmpOut;
            inputStream = tmpInput;
            connection.changeState(Connection.STATE_CONNECTED, true);
            if (callback != null) {
                callback.onSuccess();
            }
            connection.callback(
                    MethodInfoGenerator.onConnectionStateChanged(device,
                            Connection.STATE_CONNECTED)
            );
        });
    }

    private void onConnectFail(final ConnectionImpl connection,
                               final ConnectCallback callback,
                               final String errMsg,
                               final IOException e) {
        connection.changeState(Connection.STATE_DISCONNECTED, true);
        if (BTManager.isDebugMode) {
            Log.w(BTManager.DEBUG_TAG, errMsg);
        }
        close();
        if (callback != null) {
            callback.onFail(errMsg, e);
        }
        connection.callback(MethodInfoGenerator.onConnectionStateChanged(device, Connection.STATE_DISCONNECTED));
    }

    public int read(byte[] buffer) {
        if (inputStream != null && !connection.isReleased()) {
            try {
                return inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }



    void write(WriteData data) {
        if (outStream != null && !connection.isReleased()) {
            try {
                outStream.write(data.value);
                BTLogger.INSTANCE.d(BTManager.DEBUG_TAG, "Write success. tag = " + data.tag);
                if (data.callback == null) {
                    connection.callback(MethodInfoGenerator.onWrite(device, data.tag, data.value, true));
                } else {
                    data.callback.onWrite(device, data.tag, data.value, true);
                }
            } catch (IOException e) {
                onWriteFail("Write failed: " + e.getMessage(), data);
            }
        } else {
            onWriteFail("Write failed: OutputStream is null or connection is released", data);
        }
    }

    private void onWriteFail(String msg, WriteData data) {
        if (BTManager.isDebugMode) {
            Log.w(BTManager.DEBUG_TAG, msg);
        }
        if (data.callback == null) {
            connection.callback(MethodInfoGenerator.onWrite(device, data.tag, data.value, false));
        } else {
            data.callback.onWrite(device, data.tag, data.value, false);
        }
    }

    void close() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (Throwable e) {
                BTLogger.INSTANCE.e(BTManager.DEBUG_TAG, "Could not close the client socket: " + e.getMessage());
            }
        }
    }

    boolean isConnected() {
        return socket != null && socket.isConnected();
    }


    static class WriteData {
        String tag;
        byte[] value;
        WriteCallback callback;

        WriteData(String tag, byte[] value) {
            this.tag = tag;
            this.value = value;
        }
    }
}
