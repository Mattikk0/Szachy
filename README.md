Wersja 1.0

(+) rysowanie figur na planszy, odwzorowanie planszy, roszada, en passant, szach (wieże i gońce), bicia

Wersja 1.1

(+) szach (wszystkie figury), filtrowanie ruchów króla (początkowe)

Wersja 1.2

(+) sprawdzenie czy ruch/bicie nie powoduje szacha (dla wszystkich figur i pionków), odfiltrowanie nielegalnych ruchów króla, dodany "zone" króla, możliwe znajdowanie położenia figury na pdstawie klasy i koloru.

(m) poprawiony błąd w roszadzie od strony króla, zmiany w filtrowaniu ruchw króla, zmienione en Passant, zmiany w funkcjach isChecking()

(-) usunięcie funkcji getCheckPath()

Wersja 1.3

(+) mat, pat(brak ruchów), haszowanie pozycji na szachownicy do jednej liczby

(-) usunięte kilka niepotrzebnych (po usprawnieniu działania szacha) rzeczy

(!) wywaliło jakiś błąd w trakcie gry nie zidentyfikowany, nie wiadomo co go spowodowało, po uruchomieniu ponownie nigdy się nie powtórzył. Wywaliło go po zrobieniu rozszady w kingside białego i po kolejnym ruchu czarnego po kliknięciu jakiejkolwiek figury wyrzucało cały czas ten sam błąd i nie można było ruszyć.

Wersja 1.4

(+) Zapis i odczyt partii z pliku .txt . 

(m) Zmienione rozpoczęcie gry (zależy od boola czy nowa gra czy wczytanie starej)

Wersja 1.5

(+) pat(50 ruchów bez bicia, 3 powtórzenia pozycji, brak materiału do mata), zapisywanie dodatkowych informacji o stanie partii (licznik ruchów bez bicia/szacha, tura gracza, punkty materiału graczy)

(-) usunięta metoda getState() w GameState.

(m) Zapisywanie po każdym ruchu zamiast po kliknięciu klawisza "s".

(?) Wstępnie zrobione: zapisywanie informacji który pionek ruszył się o 2 pola i czy król został ruszony (do roszady ale trzeba dodać czy wieże zostały ruszone).

Wersja 1.6

(m) Zmieniony zapis gry na zapis w pliku .pgn w formacie notacji FEN (Do poprawy bo wczytywanie nie działa, sprawdzić zapis en Passant i roszady)

(+) Promocja pionka

Wersja 1.7

(+) Zrobione podstawowe menu z wyborem nowej gry/wczytywania gry (GUI - tkinter), do połowy zrobione menu końcowe, do zrobienia jest restart gry po zakończeniu.

(m) Wczytywanie z pliku .pgn, poprawione wczytywanie en Passant

Wersja 1.8

(+) Menu do wyświetlania komunikatów (na razie 2 komunikaty: brak zapisanej gry, otwarcie zapisanej gry która została już ukończona), bez wyboru wczytania gry/rozpoczęcia nowej gry nie wyświetla się już plansza, do zrobienia dalej restart po zakończeniu gry.

(m) poprawione wyświetlanie ostatniego ruchu przed końcem gry

Wersja 1.9

(+) Działający przcisk "Restart" w EndMenu wyświetla StartMenu. Dodany bot szachowy, który losuje figurę i ruch jaki wykona.

Wersja 2.0

(+) Dodane menu wyboru koloru, przeciwnika i poziomu bota, dodane ruchy bota, wstepnie zrobiona promocja pionka dla bota (lvl 0)

Wersja 2.1

(m) zmieniona roszada (działa gdy czarny jest na dole planszy). Gra wczytuje już rozgrywkę z botem tak że bot wykonuje juz ruchy po wczytaniu. Promocja dla bota działa.Zmiany w zapsie do pliku i w odczycie z pliku (informacje o tym kto gra jakim kolorem i który kolor jest na dole planszy)

Wersja 2.2

(+) Bot poziom 1 - algorytm minimax który uwzględnia różne aspekty podczas obliczania siły danego ruchu. Dla każdej figury dodana osobna funkcja obliczająca właśnie siłę tego ruchu.

(!) Do poprawy jest liczenie siły ruchu ponieważ bot zwraca bardzo duże liczby mimo że niekoniecznie moga one wyjść z tych działań.