package ru.vzotov.cashreceipt.interfaces.accounting.facade;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.CheckItemNotFoundException;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.CheckDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.TimelineDTO;

import java.time.LocalDate;
import java.util.List;

public interface CashreceiptsFacade {

    /**
     * Получение всех чеков в заданном интервале дат
     *
     * @param fromDate начальная дата (включается в интервал)
     * @param toDate   конечная дата (включается в интервал)
     * @return список чеков
     */
    List<CheckDTO> listAllChecks(LocalDate fromDate, LocalDate toDate);

    /**
     * Получение всех зарегистрированных кодов чеков в заданном интервале дат
     *
     * @param fromDate начальная дата чека (включительно)
     * @param toDate   конечная дата чека (включительно)
     * @return список кодов чеков
     */
    List<QRCodeDTO> listAllCodes(LocalDate fromDate, LocalDate toDate);

    /**
     * Формирование актуального таймлайна.
     * Система подсчитывает количество чеков за каждый месяц, возвращает все месяцы где есть чеки и количество чеков.
     *
     * @return таймлайн
     */
    TimelineDTO getTimeline();

    /**
     * Get check from persistent storage
     *
     * @param qrCodeData
     * @return
     * @deprecated use getCheck(String qrCodeData)
     */
    @Deprecated
    CheckDTO loadCheck(String qrCodeData);

    /**
     * Get check from persistent storage
     *
     * @param qrCodeData QR code
     * @return check
     */
    CheckDTO getCheck(String qrCodeData);

    /**
     * Попытаться загрузить чек из внешних систем
     * @param qrCodeData
     * @return
     */
    CheckDTO loadCheckDetails(QRCodeData qrCodeData);

    /**
     * Назначение категории для позиции чека
     *
     * @param checkId     суррогатный идентификатор чека
     * @param itemIndex   номер позиции
     * @param newCategory категория, которую нужно назначить позиции
     * @throws CheckNotFoundException     если чек не найден
     * @throws CheckItemNotFoundException если позиция в чеке не найдена
     */
    void assignCategoryToItem(CheckId checkId,
                              Integer itemIndex,
                              String newCategory) throws CheckNotFoundException, CheckItemNotFoundException;

    /**
     * Получение списка категорий
     *
     * @return список категорий
     */
    List<PurchaseCategoryDTO> getAllCategories();

    /**
     * Получение категории
     *
     * @param id идентификатор категории
     * @return категория
     */
    PurchaseCategoryDTO getCategory(PurchaseCategoryId id);

    /**
     * Создание новой категории
     *
     * @param name название категории
     * @return категория
     */
    PurchaseCategoryDTO createNewCategory(String name);

    /**
     * Переименование категории
     *
     * @param id идентификатор
     * @param newName новое название
     * @return категория
     */
    PurchaseCategoryDTO renameCategory(PurchaseCategoryId id, String newName);
}
