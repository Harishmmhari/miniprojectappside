import  cv2
for i in range(32):
    filename="depthimage"+str(i)+".png"
    filename2="depthimagegray"+str(i)+".png"
    img=cv2.imread(filename,0)
    img=cv2.resize(img,(600,400))
    cv2.imwrite(filename2,img)

for i in range(32):
    filename="colorimage"+str(i)+".png"
    filename2="colorimageresized"+str(i)+".png"
    img=cv2.imread(filename)
    img=cv2.resize(img,(600,400))
    cv2.imwrite(filename2,img)

