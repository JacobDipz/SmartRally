import torch
import torch.nn as nn
import torch.nn.functional as F
import csv
import numpy as np
import torch
import torch.nn as nn
import torch.optim as optim
from sklearn.model_selection import train_test_split


class SmartRallyNet(nn.Module):
    def __init__(self):
        super(SmartRallyNet, self).__init__()
        self.fc1 = nn.Linear(5, 64)    
        self.fc2 = nn.Linear(64, 128)  
        self.fc3 = nn.Linear(128, 64)  
        self.fc4 = nn.Linear(64, 2)    

    def forward(self, x):
        x = F.relu(self.fc1(x))  
        x = F.relu(self.fc2(x))  
        x = F.relu(self.fc3(x))  
        x = self.fc4(x)         
        return x

x, y = [], []
with open("datasett.csv", "r", encoding="utf-8") as file:
    reader = csv.reader(file)
    for row in reader:
        if row:
            x.append([int(r) for r in row[:-1]])
            y.append(int(row[-1]))

X = np.array(x)
y = np.array(y)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

X_train_tensor = torch.tensor(X_train, dtype=torch.float32).unsqueeze(-1)
y_train_tensor = torch.tensor(y_train, dtype=torch.long)
X_test_tensor = torch.tensor(X_test, dtype=torch.float32).unsqueeze(-1)
y_test_tensor = torch.tensor(y_test, dtype=torch.long)

epochs = 1000


model = SmartRallyNet()
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters())

best_test_acc = 0
for epoch in range(epochs):
    model.train()
    outputs = model(X_train_tensor)
    loss = criterion(outputs, y_train_tensor)
    optimizer.zero_grad()
    loss.backward()
    optimizer.step()

    _, predicted = torch.max(outputs, 1)
    train_acc = (predicted == y_train_tensor).float().mean().item()

    model.eval()
    with torch.no_grad():
        test_preds = model(X_test_tensor)
        test_acc = (torch.argmax(test_preds, 1) == y_test_tensor).float().mean().item()
    
    if test_acc > best_test_acc:
        best_test_acc = test_acc
        torch.save(model.state_dict(), "model.pth")

    print(f"epoch {epoch+1}/{epochs} loss{loss.item()} train : {train_acc} test : {test_acc}")

model.load_state_dict(torch.load("model.pth"))

model.eval()
with torch.no_grad():
    final_train_preds = model(X_train_tensor)
    final_train_acc = (torch.argmax(final_train_preds, 1) == y_train_tensor).float().mean().item()

    final_test_preds = model(X_test_tensor)
    final_test_acc = (torch.argmax(final_test_preds, 1) == y_test_tensor).float().mean().item()

print(f"training: {final_train_acc}")
print(f"testing : {final_test_acc}")

def predict_sequence(model, sequence):
    model.eval()
    with torch.no_grad():
        input_tensor = torch.tensor(sequence, dtype=torch.float32).unsqueeze(0).unsqueeze(-1)
        output = model(input_tensor)
        predicted = torch.argmax(output, dim=1).item()
        return predicted
