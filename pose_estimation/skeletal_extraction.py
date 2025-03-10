from ultralytics import YOLO
import cv2

model = YOLO('yolo11m-pose.pt') #use higher model later (yolo11x-pose.pt)

court_coords = (400, 315-75, 1500, 900) #coordinates from yolo model --> might wanna actually have the model run here 

#results = model(source="mentoo.mp4", show=False, conf=0.3, save=True) # can use f string! >:D 
results = model(source=f"{(vid:=("match2"))}.mp4", show=False, conf=0.3, save=True) # + walrus <3

output = cv2.VideoWriter(f"{vid}_bounded.mp4", cv2.VideoWriter_fourcc(*'mp4v'), 30, (1920, 1080)) 

for result in results:
    frame = result.orig_img  
    
    x1, y1, x2, y2 = court_coords

    for i, pose in enumerate(result.keypoints):
        #maybe switch to a csv file --> would be easier to read back in
        #f.write(f"Player {i}: ") #player " + i + " ") #move down, want only the actual players' data extraction
        # keypoints = pose.xy  いらん
        
        for index, keypoint in enumerate(p:=(pose.xy[0])):
            x, y = keypoint[0].item(), keypoint[1].item()

            if x > 0 and y > 0 and x1 <= x <= x2 and y1 <= y <= y2:
                # f = open(f"{vid}_player{i}.txt", "a") #vid + "player" + str(i) + ".txt", "a") 
                # f.write(f"({x},{y}), ")
                # if index == len(p)-1:
                #     f.write("\n")
                # f.close()
                cv2.circle(frame, (int(x), int(y)), 5, (0, 0, 255), -1)  

    output.write(frame)

output.release()
cv2.destroyAllWindows()