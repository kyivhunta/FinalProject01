package com.firstgroup.project.consoleHelper;

import com.firstgroup.project.APIs.Controller;
import com.firstgroup.project.DAOs.CommonDAO;
import com.firstgroup.project.Exceptions.*;
import com.firstgroup.project.hotels.Hotel;
import com.firstgroup.project.hotels.Room;
import com.firstgroup.project.hotels.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sonikb on 22.04.2017.
 */
public class ConsoleHelper {
    Controller controller = new Controller();
    BufferedReader buffRead = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) {
        ConsoleHelper consoleHelper = new ConsoleHelper();
        consoleHelper.mainMenu();
//        consoleHelper.loginService();
    }

    public void mainMenu() {

        System.out.println("-->Система бронирования отелей<--" + "\n");

        System.out.println("1. * @Добавить отель");
        System.out.println("2. * Редактировать данные отеля");
        System.out.println("3. * @Добавить комнату в отель");
        System.out.println("4. * Редактировать данные комнаты");
        System.out.println("5. * @Удалить комнату из отеля");
        System.out.println("6. * @Удалить отель");
        System.out.println("7. * @Зарегистрировать пользователя");
        System.out.println("8. * @Редактировать данные пользователя");
        System.out.println("9. * @Удалить пользователя");
        System.out.println("10. * Поиск отеля по имени");
        System.out.println("11. * Поиск отеля по городу");
        System.out.println("12. * Поиск комнаты по отелю");
        System.out.println("13. * Бронирование комнаты на имя пользователя");
        System.out.println("14. * Отмена бронирования комнаты" + "\n");
        System.out.println("0. * ВЫХОД");
        chooseTheOperation();
    }

    private void chooseTheOperation() {
        System.out.println("Введите номер операции которую вы хотите произвести!!!");
        try {
            switch (Integer.parseInt(buffRead.readLine())) {
                case 1:
                    System.out.println("\n***** Добавление отеля в базу данных *****\n");
                    addHotel();
                    mainMenu();
                    break;
                case 2:
                    System.out.println("\n***** Редактирование данных отеля *****\n");
                    editHotelDetails();
                    mainMenu();
                    break;
                case 3:
                    System.out.println("\n***** Добавление комнаты в отель *****\n");
                    addRoom();
                    mainMenu();
                    break;
                case 4:
                    System.out.println("\n***** Редактирование данных комнаты *****\n");
                    editRoomInfo();
                    mainMenu();
                    break;
                case 5:
                    System.out.println("\n***** Удалиение комнат из отеля *****\n");
                    deleteRoom();
                    mainMenu();
                    break;
                case 6:
                    System.out.println("\n***** Удаление отелей *****\n");
                    deleteHotel();
                    mainMenu();
                    break;
                case 7:
                    System.out.println("\n***** Регистрация пользователей *****\n");
                    addUser();
                    mainMenu();
                    break;
                case 8:
                    System.out.println("\n***** Редактирование данных пользователя *****\n");
                    editUserInfo();
                    mainMenu();
                    break;
                case 9:
                    System.out.println("\n***** Удаление пользователей *****\n");
                    deleteUser();
                    mainMenu();
                    break;
                case 10:
                    System.out.println("\n***** Поиск отеля по имени *****\n");
                    findByNameHotel();
                    mainMenu();
                    break;
                case 11:
                    System.out.println("\n***** Поиск отеля по городу *****\n");
                    findByCity();
                    mainMenu();
                    break;
                case 12:
                    System.out.println("\n***** Поиск комнаты по отелю *****\n");
                    findRoomsByHotel();
                    mainMenu();
                    break;
                case 13:
                    System.out.println("\n***** Бронирование комнаты на имя пользователя *****\n");
                    reservationRoom();
                    mainMenu();
                    break;
                case 14:
                    System.out.println("\n***** Отмена бронирования комнаты *****\n");
                    cancelReservation();
                    mainMenu();
                    break;
                case 0:
                    System.out.println("\n***** Программа завершена, все изменения сохранены *****\n");
                    CommonDAO.save();
                    buffRead.close();
                    break;
                default:
                    System.out.println("Не верный номер операции! Повторите попытку!" + " \nДля выхода нажмите \"0\"");
                    chooseTheOperation();
            }
        } catch (IOException e) {
            e.printStackTrace();              //TODO create some exception
        } catch (NoSuchElementException | NumberFormatException n) {
            System.out.println("Не верный номер операции! Повторите попытку!" + " \nДля выхода нажмите \"0\"");
            chooseTheOperation();
        }
    }

    private void addHotel() {
        try {
            System.out.println("Укажите название отеля:");
            String hotelName = buffRead.readLine();
            System.out.println("Укажите название города:");
            String cityName = buffRead.readLine();
            System.out.println("Для создания отеля необходимо создать хотя бы одну комнату!");
            System.out.println("Укажите количество спальных мест в номере:");
            int roomPersons = Integer.parseInt(buffRead.readLine());
            System.out.println("Укажите цену номера в грн/сутки:");
            double roomPrice = Double.parseDouble(buffRead.readLine());
            System.out.println("Укажите дату когда номер будет доступен в формате year.mm.dd");
            String dateAvailableFrom = buffRead.readLine();
            Hotel hotel = controller.addHotel(hotelName, cityName, roomPersons, roomPrice, dateAvailableFrom);
            System.out.println(hotel.getHotelName() + " успешно сохранен!");
        } catch (HotelAlreadyExist r) {
            r.getMessage();
            addHotel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRoom() {
        System.out.println("Выберите отель в котором вы хотите добавить комнату:");
        int count = 1;
        List<String> hotelNames = controller.getCommonDAO().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        try {
            int hotelIndex = Integer.parseInt(buffRead.readLine()) - 1;
            Hotel hotel = controller.getCommonDAO().getDataBase().getHotelList().get(hotelIndex);
            System.out.println("**** Добавление комнат в отель " + hotel.getHotelName() + " ****");
            System.out.println("Укажите количество спальных мест в номере:");
            int roomPersons = Integer.parseInt(buffRead.readLine());
            System.out.println("Укажите цену номера в грн:");
            double roomPrice = Integer.parseInt(buffRead.readLine());
            System.out.println("Укажите дату когда номер будет доступен в формате year.mm.dd");
            String dateAvailableFrom = buffRead.readLine();
            controller.addRoom(hotelIndex, roomPersons, roomPrice, dateAvailableFrom);
            System.out.println("Комната сохранена в отель " + hotel.getHotelName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteHotel() {
        System.out.println("Список отелей : ");
        int count = 1;
        List<String> hotelNames = controller.getCommonDAO().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        try {
            System.out.println("Введите номер отеля которий вы хотите удалить: ");
            int hotelIndex = Integer.parseInt(buffRead.readLine()) - 1;
            controller.deleteHotel(hotelIndex);
            System.out.println("Список отелей после удаления: ");
            List<String> hotelNamesWhenWeDeleteHotel = controller.getCommonDAO().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
            int count1 = 1;
            for (String hotel : hotelNamesWhenWeDeleteHotel) {
                System.out.println(count1++ + ". * " + hotel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRoom() {
        System.out.println("Выберете номер отеля из которого вы хотите удалить комануту: ");
        int count = 1;
        List<String> hotelNames = controller.getCommonDAO().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        for (String hotelName : hotelNames) {
            System.out.println(count++ + ". * " + hotelName);
        }
        try {
            System.out.println("Введите номер отеля из которого ви хотите удалить:");
            int hotelIndex = Integer.parseInt(buffRead.readLine()) - 1;
            Hotel hotel = controller.getCommonDAO().getDataBase().getHotelList().get(hotelIndex);
            System.out.println("**** Удаление комнат в отеле " + hotel.getHotelName() + " ****");
            count = 1;
            for (Room room : hotel.getRoomList()) {
                System.out.println(count++ + ". * " + room);
            }
            System.out.println("Введите номер комнати которою ви хотите удалить: ");
            int i = Integer.parseInt(buffRead.readLine());
            Room room = hotel.getRoomList().get(i - 1);
            controller.deleteRoom(room);
            System.out.println("**** Новий список комант в " + hotel.getHotelName() + " ****");
            List<Room> roomList1 = new ArrayList<>(hotel.getRoomList());
            count = 1;
            for (Room room1 : roomList1) {
                System.out.println(count++ + ". * " + room1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editHotelDetails() {
        int count = 1;
        System.out.println("Выберите из списка номер отеля который необходимо отредактировать:");
        List<Hotel> hotelList = controller.getCommonDAO().getDataBase().getHotelList();
        for (Hotel hotel : hotelList) {
            System.out.println(count++ + ". * " + hotel.getHotelName());
        }
        try {
            int hotelIndex = Integer.parseInt(buffRead.readLine()) - 1;
            System.out.println("Название отеля " + hotelList.get(hotelIndex).getHotelName() + " изменяем на: ");
            String newHotelName = buffRead.readLine();
            System.out.println("Название города " + hotelList.get(hotelIndex).getCityName() + " изменяем на: ");
            String newCityName = buffRead.readLine();
            Hotel editedHotel = controller.editHotelDetails(hotelIndex, newHotelName, newCityName);
            System.out.println("Изменениея успешно сохранены: имя отеля " + editedHotel.getHotelName() + ", город: " + editedHotel.getCityName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editUserInfo() {
        System.out.println("Список юзеров в системе: ");
        Set<Map.Entry<String, User>> entrySet = controller.getCommonDAO().getDataBase().getUserMap().entrySet();
        //получаем набор елементов
        // Отобразим набор
        int count = 1;
        List<String> emailList = new ArrayList<>();
        for (Map.Entry<String, User> userEntry : entrySet) {
            System.out.println(count++ + ". * " + userEntry.getValue());
            emailList.add(userEntry.getValue().getEmail());
        }
        try {
            System.out.println("Введите номер пользователя которого вы хотите редактирувать: ");
            int emailIndex = Integer.parseInt(buffRead.readLine()) - 1;
            String oldEmail = emailList.get(emailIndex);
            System.out.println("Введите новое имя пользувателя: ");
            String newName = buffRead.readLine();
            System.out.println("Введите новою фамилию пользувателя: ");
            String newSurName = buffRead.readLine();

            System.out.println("Параметры пользувателя после редактирувания: \n"
                    + controller.editUserInfo(newName, newSurName, oldEmail));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editRoomInfo() {
        System.out.println("Список отелей в системе: ");
        int count = 1;
        List<String> hotelNames = controller.getCommonDAO().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());

        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        try {
            System.out.println("Введите номер отеля в котором вы хотите редактирувать комнаты");
            int hotelIndex = Integer.parseInt(buffRead.readLine()) - 1;
            Hotel hotel = controller.getCommonDAO().getDataBase().getHotelList().get(hotelIndex);
            System.out.println("**** Список комнат в " + hotel.getHotelName() + " ****");
            count = 1;
            for (Room room : hotel.getRoomList()) {
                System.out.println(count++ + ". * " + room);
            }
            System.out.println("Введите номер комнати которою ви хотите редактирувать: ");
            int roomIndex = Integer.parseInt(buffRead.readLine()) - 1;
            Room room = hotel.getRoomList().get(roomIndex);
            System.out.println("**** Редактирувание параметров комнати  ****");
            System.out.println("Укажите количество спальных мест в номере:");
            int roomPersons = Integer.parseInt(buffRead.readLine());
            System.out.println("Укажите цену номера в грн:");
            double roomPrice = Double.parseDouble(buffRead.readLine());
            System.out.println("Укажите дату когда номер будет доступен в формате year.mm.dd");
            String dateAvailableFrom = buffRead.readLine();
            System.out.println("Комната после введеных изменений: \n" +
                    controller.editRoomDetails(hotelIndex, roomIndex, roomPersons, roomPrice, dateAvailableFrom));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginService() {
        System.out.println("Чтобы войти в систему создайте профиль или выполните вход с существуещего!");
        System.out.println("\n1. * Зарегистрироваться!");
        System.out.println("2. * Войти!");
        try {
            String line = buffRead.readLine();
            if (!"1".equals(line) && !"2".equals(line)) {
                System.out.println("Не верная команда, повторите попытку!\n");
                loginService();
            }
            if ("1".equals(line)) {
                regUser();
                mainMenu();
            } else {
                enterToSystem();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enterToSystem() {
        System.out.println("***Вход в систему***");
        String email;
        String password;
        try {
            System.out.println("Введите email: ");
            email = buffRead.readLine();
            System.out.println("Введите password: ");
            password = buffRead.readLine();
            if (controller.loginUser(email, password)) {
                System.out.println("Вход выполнен " + controller.getCommonDAO().getDataBase().getCurrentUser().getName() + "\n");
                mainMenu();
            }
        } catch (IncorrectEmail | IncorrectPassword ex) {
            System.out.println(ex.getMessage());
            enterToSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void regUser() {
        try {
            System.out.println("Введите Ваше имя");
            String name = buffRead.readLine();
            System.out.println("Введите Вашу фамилию");
            String secondName = buffRead.readLine();
            System.out.println("Введите Ваш email");
            String email = buffRead.readLine();
            System.out.println("Введите PASSWORD");
            String password = buffRead.readLine();

            User user = controller.registerUser(name, secondName, email, password);
            System.out.println("Пользователь " + user.getEmail() + " успешно зарегистрирован!\n");
        } catch (UserAlreadyExist e) {
            System.out.println(e.getMessage());
            regUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUser() {
        try {
            System.out.println("Введите Ваше имя");
            System.out.println("\nДля возврата в меню введите \"0\"");
            String name = buffRead.readLine();
            if ("0".equals(name)) return;
            System.out.println("Введите Вашу фамилию");
            String secondName = buffRead.readLine();
            System.out.println("Введите Ваш email");
            String email = buffRead.readLine();
            System.out.println("Введите PASSWORD");
            String password = buffRead.readLine();

            User user = controller.addUser(name, secondName, email, password);
            System.out.println("Пользователь " + user.getEmail() + " успешно зарегистрирован!\n");
        } catch (UserAlreadyExist e) {
            System.out.println(e.getMessage());
            regUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        System.out.println("Cписок пользователей----------------------------------------\n");
        Set<Map.Entry<String, User>> entries = controller.getCommonDAO().getDataBase().getUserMap().entrySet();
        int count = 1;
        List<String> emailList = new ArrayList<>();
        for (Map.Entry<String, User> userEntry : entries) {
            System.out.println(count++ + "." + " " + userEntry.getValue());
            emailList.add(userEntry.getValue().getEmail());
        }
        try {
            System.out.println("\nУкажите номер пользователя которого хотите удалить");
            System.out.println("\nДля возврата в меню введите \"0\"");
            int emailIndex = Integer.parseInt(buffRead.readLine()) - 1;
            System.out.println("Пользователь " + controller.deleteUser(emailList.get(emailIndex)) + " удалён!\n");
            count = 1;
            for (Map.Entry<String, User> entry : entries) {
                System.out.println(count++ + ". " + entry.getValue());
            }
            if (emailIndex == -1) return;
        } catch (UserNotCreated | CantDeleteCurrentUser ex) {
            System.out.println(ex.getMessage());
            deleteUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List findByNameHotel() {
        try {
            System.out.println("Введите название отеля: ");
            String name = buffRead.readLine();
            CommonDAO commonDAO = new CommonDAO();
            commonDAO.findHotelByName(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List findByCity() {
        try {
            System.out.println("Введите название города: ");
            String city = buffRead.readLine();
            CommonDAO commonDAO = new CommonDAO();
            commonDAO.findHotelByCity(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List findRoomsByHotel() {
        try {
            System.out.println("Введите название отеля: ");
            String hotel = buffRead.readLine();
            //System.out.println("Введите 1,если Вы хотите ввести дополнительные критерии подбора комнаты,либо 2,если продолжить поиск без них ");
            //String answer = sc.nextLine();
//
            //if (answer.equals("1")) {
            //    System.out.println("Введите количество спальных мест ");
            //    int persons = sc.nextInt();
            //    System.out.println("Введите максимально допустимую для Вас цену ");
            //    Double price = sc.nextDouble();
            //    System.out.println("Введите дату прибытия в отель в формате year.mm.dd ");
            //    sc.nextLine();
            //    String dateAvailableFrom = sc.nextLine();
            //    CommonDAO commonDAO = new CommonDAO();
            //    commonDAO.findRoomsByHotel(hotel,persons,price,dateAvailableFrom);
//
            //}
            //if (answer.equals("2")) {
            CommonDAO commonDAO = new CommonDAO();
            commonDAO.findRoomsByHotel(hotel);
            //}else System.out.println("Неверный формат ввода");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    private void reservationRoom() {
        System.out.println("Виберете отель в которогм вы хотите забронировать комануту: ");
        int count = 1;
        List<String> hotelNames = controller.getCommonDAO().getDataBase().getHotelList().stream().map(Hotel::getHotelName).collect(Collectors.toList());
        for (String hotel : hotelNames) {
            System.out.println(count++ + ". * " + hotel);
        }
        try {
        System.out.println("Введите номер отеля в котором вы хотите забронировать комнату:");
        System.out.println("Для выхода введите \"0\".");
        int hotelIndex = Integer.parseInt(buffRead.readLine()) - 1;
        if (hotelIndex == -1) return;
        Hotel hotel = controller.getCommonDAO().getDataBase().getHotelList().get(hotelIndex);
        System.out.println("**** Бронирование комнат в отеле " + hotel.getHotelName() + " ****");
        count = 1;
        for (Room room : hotel.getRoomList()) {
            System.out.println(count++ + ". * " + room);
        }
        System.out.println("Введите номер комнати которою ви хотите забронировать: ");
        int i = Integer.parseInt(buffRead.readLine());



            controller.roomReservationByName(hotelIndex, i - 1);
            hotel.getRoomList().stream().forEach(System.out::println);
            System.out.println(controller.getCommonDAO().getDataBase().getCurrentUser().getRoomList());
        } catch (InvalidRoomStatus | InvalidHotelStatus invalidStatus) {
            System.out.println(invalidStatus.getMessage());
            reservationRoom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancelReservation() {
        int count = 1;
        List<Room> userRooms = controller.getCommonDAO().getDataBase().getCurrentUser().getRoomList();
        for (Room room : userRooms) {
            System.out.println(count++ + ". * " + room);
        }
        try {
            System.out.println("Введите номер комнаты с которой вы хотите снять бронь: ");
            System.out.println("Для выхода введите \"0\".");
            int roomIndex = Integer.parseInt(buffRead.readLine()) - 1;
            if (roomIndex == -1) return;

            if (controller.cancelReservationByName(roomIndex)) {
                System.out.println("Бронь отменена!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}