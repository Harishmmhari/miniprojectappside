package com.google.ar.core.codelab.rawdepth;

import android.media.Image;
import android.opengl.Matrix;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.CameraIntrinsics;
import com.google.ar.core.Frame;
import com.google.ar.core.exceptions.NotYetAvailableException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class datasend {
    private static final String TAG = "depthlength";

    public static void create(Frame frame, Anchor cameraPoseAnchor) {
        try (Image depthImage=frame.acquireDepthImage();
             Image cameraImage = frame.acquireCameraImage();


            )
            {
                Log.d(TAG, "depthimage: start");
                sendimage(depthImage);
                Log.d(TAG, "depthimage: stop");
                depthImage.close();


                Log.d(TAG, "cameraimage: start");
                yuv420ToBitmap(cameraImage);
                cameraImage.close();
                cameraImage.close();
                Log.d(TAG, "cameraimage: stop");

                final CameraIntrinsics intrinsics = frame.getCamera().getTextureIntrinsics();
                float[] modelMatrix = new float[16];
                cameraPoseAnchor.getPose().toMatrix(modelMatrix, 0);

            } catch (NotYetAvailableException notYetAvailableException) {
            notYetAvailableException.printStackTrace();
            Log.d(TAG, "depth not available yet");
        }

        //Log.d(TAG, "create: "+Arrays.deepToString(cameraImage.getPlanes()));
            //Image depthImage = frame.acquireDepthImage();







            //depthImage.close();


    }

    private static void sendimage(
            Image depth) {

        final Image.Plane depthImagePlane = depth.getPlanes()[0];
        ByteBuffer depthByteBufferOriginal = depthImagePlane.getBuffer();
        ByteBuffer depthByteBuffer = ByteBuffer.allocate(depthByteBufferOriginal.capacity());
        depthByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        while (depthByteBufferOriginal.hasRemaining()) {
            depthByteBuffer.put(depthByteBufferOriginal.get());
        }
        depthByteBuffer.rewind();
        ShortBuffer depthBuffer = depthByteBuffer.asShortBuffer();





        final int depthWidth = depth.getWidth();
        final int depthHeight = depth.getHeight();
        //Log.d(TAG, "convertRawDepthImagesTo3dPointBuffer: "+"height:"+depthHeight+"width:"+depthWidth);





        //Log.d(TAG,"intrincisinfo  fx:"+fx+" fy:"+fy+" cx:"+cx+" cy:"+cy);
        //Log.d(TAG, "convertRawDepthImagesTo3dPointBuffer: "+ "newimage");
        //String depthinfo="startimage";
        //int[][] imagesegment=new int[depthWidth][depthHeight];
        for (int y = 0; y < depthHeight; y += 1) {
            int[] imagesegment=new int[depthWidth];
            for (int x = 0; x < depthWidth; x += 1) {
                // Depth images are tightly packed, so it's OK to not use row and pixel strides.
                int depthMillimeters = depthBuffer.get(y * depthWidth + x); // Depth image pixels are in mm.

                imagesegment[x]=depthMillimeters;

                // Unprojects the depth into a 3D point in camera coordinates.



            }
            Log.d(TAG, "depthimage: "+y+"data"+Arrays.toString(imagesegment));
        }
        //Log.d(TAG, "depthimage: data"+Arrays.deepToString(imagesegment));
        //Log.d(TAG, "convertRawDepthImagesTo3dPointBuffer: "+points.array().length);

    }
    public static void yuv420ToBitmap(Image image) {
        Log.d(TAG, "cameraopener: "+"data");
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        // sRGB array needed by Bitmap static factory method I use below.
        int[] argbArray = new int[imageWidth * imageHeight];
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        yBuffer.position(0);


        ByteBuffer uvBuffer = image.getPlanes()[1].getBuffer();
        uvBuffer.position(0);
        int r, g, b;
        int yValue, uValue, vValue;
        //Log.d(TAG, "cameraimage: "+imageHeight+"width:"+imageWidth);
        for (int y = 0; y < imageHeight - 2; y=y+3) {
            int[][] camerasegment=new int[215][3];
            //int i=0;
            for (int x = 0; x < imageWidth - 2; x=x+3) {
                int yIndex = y * imageWidth + x;
                // Y plane should have positive values belonging to [0...255]
                yValue = (yBuffer.get(yIndex) & 0xff);

                int uvx = x / 2;
                int uvy = y / 2;

                int uIndex = uvy * imageWidth + 2 * uvx;
                // ^ Note that here `uvy = y / 2` and `uvx = x / 2`
                int vIndex = uIndex + 1;

                uValue = (uvBuffer.get(uIndex) & 0xff) - 128;
                vValue = (uvBuffer.get(vIndex) & 0xff) - 128;
                r = (int) (yValue + 1.370705f * vValue);
                g = (int) (yValue - (0.698001f * vValue) - (0.337633f * uValue));
                b = (int) (yValue + 1.732446f * uValue);
                r = (int) clamp(r, 0, 255);
                g = (int) clamp(g, 0, 255);
                b = (int) clamp(b, 0, 255);
                // Use 255 for alpha value, no transparency. ARGB values are
                // positioned in each byte of a single 4 byte integer
                // [AAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB]
                int[] d=new int[3];
                d[0]=r;
                d[1]=g;
                d[2]=b;
                camerasegment[x/3]=d;
                //i++;
                /*if (i==80){
                    Log.d(TAG, "cameraimage: "+"middle "+"data"+Arrays.deepToString(camerasegment));
                    i=0;
                    camerasegment=new int[80][3];
                }*/
                argbArray[yIndex] = (255 << 24) | (r & 255) << 16 | (g & 255) << 8 | (b & 255);

            }
            Log.d(TAG, "cameraimage: "+y+"data"+Arrays.deepToString(camerasegment));
        }
        image.close();



    }
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
