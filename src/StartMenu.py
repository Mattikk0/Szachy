import tkinter as tk
import jpype
import jpype.imports
from jpype import JClass
from jpype.types import *
import sys
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

if "JAVA_HOME" not in os.environ:
    os.environ["JAVA_HOME"] = r"C:\Program Files\Java\jdk-22"

classpath = [os.path.join(BASE_DIR, "out", "production", "Szachy")]

jpype.startJVM(jpype.getDefaultJVMPath(), classpath=classpath)
StartMenu = JClass("StartMenu")

SM = StartMenu()

def on_new_game(is_new):
    print(is_new)
    root.destroy()
    root.quit()

root = tk.Tk()

window_width = 800
window_height = 800
screen_width = root.winfo_screenwidth()
screen_height = root.winfo_screenheight()
x = (screen_width // 2) - (window_width // 2)
y = (screen_height // 2) - (window_height // 2)
root.geometry(f"{window_width}x{window_height}+{x}+{y}")

new_game_button = tk.Button(root, text="Nowa gra", width=20, height=5, command=lambda: on_new_game(True))
new_game_button.pack(expand=True)

load_game_button = tk.Button(root, text="Wczytaj grę",width=20, height=5, command=lambda: on_new_game(False))
load_game_button.pack(expand=True)

root.mainloop()
jpype.shutdownJVM()
