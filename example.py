#0-80, 80-120,120-175, 175-230, 230-275, 275-345, 345-390, 390-445,445-495,495-560, 560-605, 605-656

from nbm_algorithm import predict
import cv2
from locations import graph_shuttlecock_center, graph_person, get_captioned, get_centers
from ultralytics import YOLO
from court_detection import get_equations
import numpy as np
#for match.mp4

# for 8 maybe turn into a 4
farthest = {0: 15, 1: 15, 2: 12, 3: 12, 4: 15, 5: 15, 6: 12, 7: 12, 8: 3, 9: 3, 10: 0, 11: 0, 12: 3, 13: 3, 14: 0, 15: 0}
inputs =[[15, 6, 1, 11, 4], [2, 13, 13, 13, 4], [13, 10, 9, 11, 7], [7, 1, 2, 7, 1], [5, 0, 15, 7, 1], [13, 9, 7, 10, 8], [5, 1, 15, 7, 4], [12, 11, 5, 11, 8], [10, 5, 2, 8, 4], [2, 13, 11, 14, 4], [14, 5, 9, 10, 3]]#[["Short Serve", 6,1,4,4],["Smash",2,13,2,4],["Defensive Drive", 5,9,4,7],["Short Flat Shot", 14,2,8,1],["Defensive Clear", 15,15,8,1],["Defensive Drive", 6,7,5,8],["Defensive Clear", 14,15,8,4], ["Rush Shot",4,5,4,8],["Drop Shot", 10,2,7,4],["Smash",2,11,1,4], ["Cross-Court Flight", 10,9,5,3]]
actual = [["Lift", 2], ["Clear", 9], ["Flat Shot", 10],["Lift", 13], ["Defensive Drive", 13], ["Defensive Clear", 15], ["Clear", 14], ["Defensive Drive", 15], ["Dropshot", 1], ["Cross-Court Flight", 15], ["Cut", 5], ["Tap Smash", 9], ["Defensive Drive", 14]] #[["Cross-Court Flight", 12], ["Tap Smash", 11], ["Flat Shot", 8], ["Drop Shot", 0], ["Tap Smash", 12], ["Drop Shot", 0], ["Cross-Court Flight", 11], ["Short Flat Shot", 13], ["Lift", 13], ["Rush Shot", 8], ["Rush Shot", -1]]
outputs = [["Pushshot", 12], ["Clear", 3], ["Rear-Court Flat Drive", 0],["Dropshot", 3], ["Dropshot", 3], ["Smash", 0], ["Cross-Court Flight", 0], ["Cross-Court Flight", 0], ["Dropshot", 1], ["Smash", 0], ["Short Flat Shot", 15], ["Smash", 9], ["Dropshot", 14]]#[["Rear-Court Flat Drive", 15], ["Cross-Court Flight", 3], ["Rushshot", 0], ["Dropshot", 15], ["Cross-Court Flight", 12], ["Smash", 3], ["Rushshot", 12], ["Pushshot", 3]]#[['Clear', 12], ['Drop Shot', 3], ['Tap Smash', 0], ['Cross-Court Flight', 15], ['Cross-Court Flight', 15], ['Cross-Court Flight', 3], ['Cut', 15], ['Block', 0], ['Lift', 15], ['Defensive Drive', 3], ['Rush Shot', 12]]
splits = [90,151,210,294,353,420,490,552,610,677, 720, 765,820,849]#[57,115,153,197,247,303,370,415]

# vidObj = cv2.VideoCapture("match3.mp4") 


# count = 0
# success = 1

# while success: 
#     success, image = vidObj.read() 
#     cv2.imwrite(f"frames/frame{count}.png", image) 
#     count += 1

court_cords = get_equations("frames/frame50.png")#[(0.0, 463.0), (0.0, 1077.0), (-1.4779411764705883, 1342.5073529411766), (1.7330316742081449, -1733.0497737556561)]

#sections = [(1324, 644), (1081, 644), (837, 644), (594, 644), (1303, 592), (1075, 592), (848, 592), (621, 592), (1281, 540), (1070, 540), (859, 540), (648, 540), (1259, 488), (1064, 488), (869, 488), (675, 488)]


sections = get_centers(court_cords, 4)[::-1]

img = cv2.imread("frames/frame1.png")  # or from a video frame

height, width = img.shape[:2]
# for n in range(int(len(sections))):
#     cv2.circle(img, sections[n], radius=10, color=(0,0,0), thickness=-1)  # red dots
#     cv2.imwrite("8_sections.png", img)
#     input()
# cv2.imshow(img)
# input()
out = cv2.VideoWriter(f'videos/0.mp4', cv2.VideoWriter_fourcc(*'XVID'), 30,(width,height))
out.write(img)
count = 1
vid = cv2.VideoWriter(f'vide.mp4', cv2.VideoWriter_fourcc(*'XVID'), 30,(width,height))

for index in range(2,25):
    ff = cv2.imread(f"frames/frame{count}.png")
    out.write(ff)
    count +=1
    vid.write(ff)

for index, a in enumerate(actual):
    while count < splits[index]:
        actual_shot, acutal_direction = a
        predicted_shot, predicted_direction = outputs[index]
        caption = ""
        c1 = False
        c2 = False
        if actual_shot == str(predicted_shot):
            frame = graph_person(f"frames/frame{count}.png", (0,255,0))
            c1 = True
            caption1 = actual_shot
            # caption += f"CORRECT: Best possible shot, {actual_shot}, is played.\n"
        else:
            frame = graph_person(f"frames/frame{count}.png", (0,0,255))
            caption1 = (predicted_shot, actual_shot)
            # caption += f"IMPROVEMENT: The best possible shot is {predicted_shot}, but {actual_shot} was played instead.\n"
        # if acutal_direction < len(sections)/2:
        #     ad = 15-acutal_direction
        # if predicted_direction < len(sections)/2:
        #     pd = 15-predicted_direction
        if acutal_direction != -1: # if not end game
            if acutal_direction == predicted_direction:
                c2 = True
                caption2 = acutal_direction
                frame = graph_shuttlecock_center(acutal_direction,frame, sections,(0,255,0))
                # caption += f"CORRECT: The best possible direction is section {acutal_direction} of the opponents court. "
            else:
                caption2 = (predicted_direction,acutal_direction)
                frame = graph_shuttlecock_center(acutal_direction,frame, sections,(0,0,255))
                frame = graph_shuttlecock_center(predicted_direction,frame, sections,(0,255,0))
                # caption += f"IMPROVMENT: Best possible direction is section {predicted_direction}, but you played {acutal_direction}."
        frame = get_captioned(frame, c1,c2,caption1,caption2)
        count+=1
        out.write(frame)
        vid.write(frame)
    if index+1 != len(actual): #should only be 10 total
        out = cv2.VideoWriter(f'videos/{index+1}.mp4', cv2.VideoWriter_fourcc(*'XVID'), 30,(width,height))

    

