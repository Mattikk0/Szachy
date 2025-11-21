import tkinter as tk
import jpype
import jpype.imports
from jpype import JClass
from jpype.types import *
import sys

classpath = ["C:/Users/matil/IdeaProjects/Szachy/out/production/Szachy"]

jpype.startJVM("C:/Program Files/Java/jdk-22/bin/server/jvm.dll", classpath=classpath)
EndMenu = JClass("EndMenu")

EM = EndMenu()

def game_over(restart):
    print(restart)
    root.destroy()
    root.quit()

root = tk.Tk()

window_width = 400
window_height = 400
screen_width = root.winfo_screenwidth()
screen_height = root.winfo_screenheight()
x = (screen_width // 2) - (window_width // 2)
y = (screen_height // 2) - (window_height // 2)
root.geometry(f"{window_width}x{window_height}+{x}+{y}")

winner = sys.argv[1]
if(winner == "null"):
    text = "Koniec gry, remis"
else:
    if(winner == "WHITE"):
        text = "Koniec gry, wygrana białych"
    else:
        text = "Koniec gry, wygrana czarnych"

game_over_label = tk.Label(root, text=text, font=("Arial", 16), fg="black")
game_over_label.pack(pady=10)


restart_button = tk.Button(root, text="Restart", width=20, height=2, command=lambda: game_over(True))
restart_button.pack(side='left', expand=True, padx=1)

exit_game_button = tk.Button(root, text="Wyjście",width=20, height=2, command=lambda: game_over(False))
exit_game_button.pack(side='left', expand=True, padx=1)

root.mainloop()
jpype.shutdownJVM()