import time

def running_text():
    text = "RUNNING TEXT"
    for i in range(len(text) + 1):
        print("\r" + text[:i], end="")
        time.sleep(0.2)

running_text()