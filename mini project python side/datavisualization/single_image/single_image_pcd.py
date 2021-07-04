from open3d.open3d.geometry import create_rgbd_image_from_color_and_depth
from open3d import *
#from matplotlib import pyplot as plt
import open3d as o3d
color=o3d.io.read_image("color1r.png")
depth=o3d.io.read_image("matdd.png")

print(depth)
print(color)
rgbd_image = create_rgbd_image_from_color_and_depth(
   color, depth)
print()
#print(rgbd_image)
# flip the orientation, so it looks upright, not upside-down
#pcd.transform([[1,0,0,0],[0,-1,0,0],[0,0,-1,0],[0,0,0,1]])
cam=o3d.camera.PinholeCameraIntrinsic()
cam.intrinsic_matrix=[[118.20072,0.00,80.72417],
                      [0.00,116.65858,45.601974],
                      [0.00,0.00,1.00]]


pcd = create_point_cloud_from_rgbd_image(
   rgbd_image,
   cam)
#print(o3d.camera.PinholeCameraIntrinsic.set_intrinsics(o3d.camera.PinholeCameraIntrinsic,width=600,height=400, fx=118.20072,fy=116.65858,cx=80.72417,cy=45.601974))
# Flip it, otherwise the pointcloud will be upside down
pcd.transform([[1, 0, 0, 0], [0, -1, 0, 0], [0, 0, -1, 0], [0, 0, 0, 1]])
o3d.visualization.draw_geometries([pcd])
o3d.io.write_point_cloud("copy.pcd", pcd)
print(geometry.compute_point_cloud_nearest_neighbor_distance(pcd))
#o3d.visualization.draw_geometries([tetra_mesh])