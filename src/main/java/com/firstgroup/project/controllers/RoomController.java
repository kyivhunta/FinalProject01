package com.firstgroup.project.controllers;

import com.firstgroup.project.daos.RoomDAOInterface;
import com.firstgroup.project.exceptions.InvalidDateFormat;
import com.firstgroup.project.exceptions.ValidStringNameException;
import com.firstgroup.project.entity.Hotel;
import com.firstgroup.project.entity.Room;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by MakeMeSm1Le- on 08.05.2017.
 */
public class RoomController implements RoomControllerInterface {

    private RoomDAOInterface roomDAO;

    public RoomController(RoomDAOInterface roomDAO) {
        this.roomDAO = roomDAO;
    }

    /**
     * Данный метод выполняет создание нового екземпляра комнаты и передает его на уровень RoomDAO в метод save(Room obj, int hotelIndex).
     *
     * @param hotelIndex  - индекс отеля для поиска в листе.
     * @param roomPersons - количевство мест в номере.
     * @param roomPrice   - цена комнаты в грн./сутки.
     * @param date        - дата когда номер будет доступен.
     * @return - возвращает объект Room, после сохранения.
     * @throws ValidStringNameException - исключение бросается если в стоках по названиям вписать символы, числа или пустую строку.
     * @throws InvalidDateFormat        - исключение бросается вбить длиннее формат данных чем указан year.mm.dd.
     */
    public Room addRoom(int hotelIndex, int roomPersons, double roomPrice, String date) throws ValidStringNameException, InvalidDateFormat {
        if (date.length() > 10) {
            throw new IndexOutOfBoundsException();
        }
        Room newRoom = new Room(roomPersons, roomPrice, LocalDate.of(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)), Integer.valueOf(date.substring(8, 10))));
        return roomDAO.save(newRoom, hotelIndex);
    }

    /**
     * Данный метод предназначен для именения полей объектов Room и передает его на уровень RoomDAO в метод update(Room obj, int hotelIndex, int roomIndex).
     *
     * @param hotelIndex  - индекс отеля для поиска в листе.
     * @param roomIndex   - индекс комнаты для поиска в листе.
     * @param roomPersons - количевство мест в номере.
     * @param roomPrice   - цена комнаты в грн./сутки.
     * @param date        - дата когда номер будет доступен.
     * @return - возвращает объект Room, после изменения.
     * @throws InvalidDateFormat - исключение бросается вбить длиннее формат данных чем указан year.mm.dd.
     */

    public Room editRoomDetails(int hotelIndex, int roomIndex, int roomPersons, double roomPrice, String date) {
        if (date.length() > 10) {
            throw new IndexOutOfBoundsException();
        }
        Room room = new Room(roomPersons, roomPrice, LocalDate.of(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)), Integer.valueOf(date.substring(8, 10))));
        return roomDAO.update(room, hotelIndex, roomIndex);
    }


    /**
     * Данный метод принимает группу параметров, которые пользователь вводит в ConsoleHelper.
     * Метод создает комнату которая является контейнером для хранения данных и передает управление на уровень ниже - в DAO.
     * Все исключительные ситуации обрабатываются на уровень выше - в ConsoleHelper.
     *
     * @param hotelIndex - индекс отеля для поиска в листе.
     * @param roomIndex  - индекс комнаты для поиска в листе.
     * @return объект типа Room
     */
    public boolean deleteRoom(int hotelIndex, int roomIndex) {
        Hotel hotel = roomDAO.getDbServiceSingleton().getDataBase().getHotelList().get(hotelIndex);
        Room room = hotel.getRoomList().get(roomIndex - 1);
        return roomDAO.delete(room);
    }

    /**
     * Данный метод предназначен для поиска комнат по определенному параметру(название отеля) и передает его на уровень RoomDAO в метод findRoomsByHotel(String hotelName).
     * Пользователь вводит параметр поиска с консоли в классе ConsoleHelper.
     *
     * @param hotelName - входящий параметр , который является параметром поиска метода.
     * @return - Возвращаемый результат метода - List<Hotel> отвечающий параметру поиска.
     */

    public List<Hotel> findRoomsByHotel(String hotelName) {
        return roomDAO.findRoomsByHotel(hotelName);
    }
}
