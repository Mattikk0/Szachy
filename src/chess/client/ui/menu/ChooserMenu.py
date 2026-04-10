import tkinter as tk
import jpype
import jpype.imports
from jpype import JClass
from jpype.types import *
import sys
from tkinter import messagebox
import os

BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../../../.."))

if "JAVA_HOME" not in os.environ:
    os.environ["JAVA_HOME"] = r"C:\Program Files\Java\jdk-22"

classpath = [os.path.join(BASE_DIR, "out", "production", "Szachy")]

jpype.startJVM(jpype.getDefaultJVMPath(), classpath=classpath)

ChooserMenu = JClass("chess.client.ui.menu.ChooserMenu")
CM = ChooserMenu()

root = tk.Tk()
bot_check_var = tk.BooleanVar(value=False)
player_check_var = tk.BooleanVar(value=False)
black_check_var = tk.BooleanVar(value=False)
white_check_var = tk.BooleanVar(value=False)


def accept():
    col = ""
    opp = ""
    lvl = ""
    text = ""
    if(not bot_check_var.get() and not player_check_var.get()):
        messagebox.showinfo("Błąd", "Wybierz przeciwnika!")
    elif(not black_check_var.get() and not white_check_var.get()):
        messagebox.showinfo("Błąd", "Wybierz kolor!")
    else:
        if (black_check_var.get()):
            col = "black"
        if (white_check_var.get()):
            col = "white"
        if (player_check_var.get()):
            opp = "player"
            lvl = "none"
        if (bot_check_var.get()):
            opp = "bot"
            lvl = str(bot_level.get())
        text = f"{col} {opp} {lvl}"
        print(text, flush=True)
        root.destroy()
        root.quit()


def update_checkboxes(check_var, opp_check_var, *args):
    if check_var.get():
        opp_check_var.set(False)

bot_check_var.trace_add("write", lambda *args: update_checkboxes(bot_check_var, player_check_var, *args))
player_check_var.trace_add("write", lambda *args: update_checkboxes(player_check_var, bot_check_var, *args))
black_check_var.trace_add("write", lambda *args: update_checkboxes(black_check_var, white_check_var, *args))
white_check_var.trace_add("write", lambda *args: update_checkboxes(white_check_var, black_check_var, *args))

def toggle_scrollbar(*args):
    if bot_check_var.get():
        bot_level.pack(fill="y")
    else:
        bot_level.pack_forget()
bot_check_var.trace_add("write", toggle_scrollbar)
player_check_var.trace_add("write", toggle_scrollbar)


opp_frame = tk.Frame(root)
opp_frame.pack(pady=5, fill="x")

scroll_frame = tk.Frame(root)
scroll_frame.pack(pady=5, fill="x")

col_frame = tk.Frame(root)
col_frame.pack(pady=5, fill="x")


window_width = 500
window_height = 500
screen_width = root.winfo_screenwidth()
screen_height = root.winfo_screenheight()
x = (screen_width // 2) - (window_width // 2)
y = (screen_height // 2) - (window_height // 2)
root.geometry(f"{window_width}x{window_height}+{x}+{y}")


choose_opponent_label = tk.Label(opp_frame, text="Przeciwnik: ", font=("Arial", 10), fg="black")
choose_opponent_label.pack(pady=10)
bot_checkbox = tk.Checkbutton(opp_frame, text="Bot", variable=bot_check_var, font=("Arial", 10))
bot_checkbox.pack(side='left', expand=True, pady=10)
player_checkbox = tk.Checkbutton(opp_frame, text="Gracz", variable=player_check_var, font=("Arial", 10))
player_checkbox.pack(side='left', expand=True, pady=10)
bot_level = tk.Scale(scroll_frame, from_=0, to=10, orient=tk.HORIZONTAL, label="Poziom bota", font=("Arial", 10))
bot_level.set(0)

choose_opponent_label = tk.Label(col_frame, text="Wybierz kolor: ", font=("Arial", 10), fg="black")
choose_opponent_label.pack(pady=10)
bot_checkbox = tk.Checkbutton(col_frame, text="Biały", variable=white_check_var, font=("Arial", 10))
bot_checkbox.pack(side='left', expand=True, pady=10)
player_checkbox = tk.Checkbutton(col_frame, text="Czarny", variable=black_check_var, font=("Arial", 10))
player_checkbox.pack(side='left', expand=True, pady=10)


ok_button = tk.Button(root, text="Ok",width=20, height=2, command=lambda: accept())
ok_button.pack(side='left', expand=True, padx=1)

root.mainloop()
jpype.shutdownJVM()