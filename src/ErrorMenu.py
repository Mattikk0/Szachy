import tkinter as tk
import jpype
import jpype.imports
from jpype import JClass
from jpype.types import *
import sys

classpath = ["C:/Users/matil/IdeaProjects/Szachy/out/production/Szachy"]

jpype.startJVM("C:/Program Files/Java/jdk-22/bin/server/jvm.dll", classpath=classpath)
ErrorMenu = JClass("ErrorMenu")

EM = ErrorMenu()

def no_file_action():
    root.destroy()
    root.quit()

def load_game_over_action(load_game_over):
    print(load_game_over)
    root.destroy()
    root.quit()

root = tk.Tk()

window_width = 400
window_height = 100
screen_width = root.winfo_screenwidth()
screen_height = root.winfo_screenheight()
x = (screen_width // 2) - (window_width // 2)
y = (screen_height // 2) - (window_height // 2)
root.geometry(f"{window_width}x{window_height}+{x}+{y}")

error_type = sys.argv[1]
match(error_type):
    case "no_file":
        text = "Nie ma zapisanej gry"
        game_over_label = tk.Label(root, text=text, font=("Arial", 16), fg="black")
        game_over_label.pack(pady=10)
        restart_button = tk.Button(root, text="Ok", width=20, height=2, command=lambda: no_file_action())
        restart_button.pack(side='left', expand=True, padx=1)
    case "game_over":
        text = "Wczytać zakończoną grę?"
        game_over_label = tk.Label(root, text=text, font=("Arial", 16), fg="black")
        game_over_label.pack(pady=10)
        restart_button = tk.Button(root, text="Tak", width=20, height=2, command=lambda: load_game_over_action(True))
        restart_button.pack(side='left', expand=True, padx=1)
        restart_button = tk.Button(root, text="Nie", width=20, height=2, command=lambda: load_game_over_action(False))
        restart_button.pack(side='left', expand=True, padx=1)


root.mainloop()
jpype.shutdownJVM()