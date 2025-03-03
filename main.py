import requests
import tkinter as tk
from datetime import datetime, timedelta
from PIL import Image, ImageTk

def get_iftar_time(city):
    city = city.lower().replace("ı", "i").replace("ç", "c").replace("ş", "s").replace("ğ", "g").replace("ü", "u").replace("ö", "o")
    url = f"https://api.collectapi.com/pray/all?data.city={city}"
    headers = {
        "content-type": "application/json",
        "authorization": "your_token_here"
    }
    try:
        response = requests.get(url, headers=headers)
        data = response.json()
        print("API Yanıtı:", data)
        if "result" in data and data["result"]:
            iftar_time_str = data["result"][4]["saat"]
            return datetime.strptime(iftar_time_str, "%H:%M").time()
        else:
            label.config(text="Şehir bulunamadı! Lütfen geçerli bir şehir seçin.")
            return None
    except Exception as e:
        print("Hata:", e)
        label.config(text="Bağlantı hatası! Lütfen tekrar deneyin.")
        return None

def update_timer():
    now = datetime.now()
    if iftar_vakti:
        iftar_time = datetime.combine(now.date(), iftar_vakti)
        remaining = iftar_time - now

        if remaining.total_seconds() > 0:
            hours, remainder = divmod(remaining.seconds, 3600)
            minutes, seconds = divmod(remainder, 60)
            label.config(text=f"İftara: {hours} saat {minutes} dk {seconds} sn")
            root.after(1000, update_timer)
        else:
            label.config(text="İftar vakti geldi! Hayırlı iftarlar.")

def set_background():
    image = Image.open("blue-mosque-istanbul.jpg")
    image = image.resize((800, 600))
    bg_image = ImageTk.PhotoImage(image)
    bg_label.config(image=bg_image)
    bg_label.image = bg_image
    root.geometry(f"{image.width}x{image.height}")

def update_city():
    global iftar_vakti
    city = city_entry.get().strip()
    iftar_vakti = get_iftar_time(city)
    if iftar_vakti:
        update_timer()

root = tk.Tk()
root.title("İftar Sayacı")

bg_label = tk.Label(root)
bg_label.place(x=0, y=0, relwidth=1, relheight=1)
set_background()

label = tk.Label(root, text="Şehir giriniz", font=("Lato", 16, "bold"), fg="white", bg="black", padx=20, pady=10)
label.pack(pady=10)

city_entry = tk.Entry(root, font=("Lato", 14, "bold"), width=20, bd=2, relief="solid")
city_entry.pack(pady=10)
city_entry.bind("<Return>", lambda event: update_city())

button = tk.Button(root, text="İftara Kalan Süreyi Gör", font=("Lato", 14, "bold"), command=update_city, relief="raised", bg="black", fg="white", width=20, height=2)
button.pack(pady=10)

iftar_vakti = None
root.mainloop()
