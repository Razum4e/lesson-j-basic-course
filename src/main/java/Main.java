import image.TextGraphicsConverter;
import image.TextGraphicsConverterImpl;
import server.GServer;

public class Main {
    public static void main(String[] args) throws Exception {

        TextGraphicsConverter converter = new TextGraphicsConverterImpl(); // Создали тут объект класса конвертера
//        GServer server = new GServer(converter); // Создаём объект сервера
//        server.start(); // Запускаем
        converter.setMaxWidth(40);
        System.out.println(converter.convert("https://sun9-86.userapi.com/impg/PBsv4BQme1V8GTXSt6EroIBgjO4B1EekQajAsQ/q46etVBA0Hg.jpg?size=1280x1187&quality=96&sign=123a8d98ed3bd4d968caeff0b7bc5eaa&type=album"));

        // Или то же, но с сохранением в файл:
        /*
        PrintWriter fileWriter = new PrintWriter(new File("converted-image.txt"));
        converter.setMaxWidth(200);
        converter.setMaxHeight(300);
        fileWriter.write(converter.convert("https://i.ibb.co/6DYM05G/edu0.jpg"));
        fileWriter.close();
        */
    }
}
