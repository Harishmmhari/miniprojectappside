# Importing required module
import os
import numpy as np

# Using system() method to execute
# shell commands
filename="temp"+str(int(np.random.random()*1000))+".txt"
#print(filename)
creating_command='adb shell am start -n com.google.ar.core.codelab.rawdepth/.RawDepthCodelabActivity | adb logcat -d| findstr "depthlength" >'+filename
#print(creating_command)
#os.system('adb shell am start -n com.google.ar.core.codelab.rawdepth/.RawDepthCodelabActivity | adb logcat | findstr "convertRawDepthImagesTo3dPointBuffer: " >temp3.txt')

print(os.path.basename('main.py'))
k=os.getcwd().replace("/","\\")
os.system("cd "+k)
print(os.system("DIR"))
k=int(input("can we give value more than 10 int"))
if k>10:
    os.system(creating_command)
else:
    exit()



