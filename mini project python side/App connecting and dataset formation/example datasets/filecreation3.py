import numpy as np
import cv2
#import matplotlib.pyplot as plt


def writedepth(f,num):
    filename="depthimage"+str(num)+".png"
    m=f.readline()
    k=[]
    while m.find("depthimage: stop")!=46:
        m=m.split("data")
        print(m)

        k.append(m[1].replace("\n","").strip("][").split(", "))
        m=f.readline()


    data=np.asarray(k,dtype=int)
    #print(data.shape)
    cv2.imwrite(filename, data)
    print("depthimage with name"+filename+"created")
    return f

def writecolor(f,num):
    filename="colorimage"+str(num)+".png"
    img=[]
    print(f.readline())
    m=f.readline()

    while m.find("cameraimage: stop")!=46:
        m=m.split("data")[1].replace("[","").replace("]","")
        m=m.split(", ")
        p=[]
        k=0
        temppoint=[]
        for i in range(len(m)-6):
            point=[int(m[i])]
            temppoint.append(point)
            k=k+1

            if k==3:
                k=0
                p.append(temppoint)
                temppoint=[]
        img.append(p)
        m=f.readline()


    img=np.asarray(img,dtype=int)
    img=np.resize(img,(160, 213, 3))
    #print(img)
    #import matplotlib.pyplot as plt
    #plt.imsave("mat1.png", img)
    cv2.imwrite(filename,img)
    print("colorimage with name"+filename+"created")
    return f

f=open("temprr147.txt")
line=f.readline()
#print(line)
count=0
while len(line)>10:
    #print(line)
    if line.find("depthimage: start")==46:

        print("image:",count,"started")
        f=writedepth(f,count)
        k=f.readline()
        if k.find("cameraimage: start")==46:
            f=writecolor(f,count)
        count=count+1
    #count=count+1
    #print(count)
    line=f.readline()
print("final",count)

