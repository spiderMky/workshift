package com.example.workshift.business.shift;

import com.example.workshift.business.exception.ActiveShiftException;
import com.example.workshift.business.exception.MaxHoursInTheSameShopWithin24HourWindowExceededException;
import com.example.workshift.business.exception.MoreThanFiveDaysInARowInTheSameShopException;
import com.example.workshift.business.exception.ShiftCreationInThePastException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
class ShiftServiceImpl implements ShiftService {

    private ShiftRepository shiftRepository;
    private final String UTC = "UTC";

    @Override
    public Shift createShift(@NonNull final CreateShiftRequest createShiftRequest) {
        final Shift shift = Shift.of(createShiftRequest);

        validateShiftCreationNotInThePast(shift);
        validateUserIsNotWorkingAtTheSameTime(shift);
        validateUserHoursInTheSameShopWithin24HourWindow(shift);
        validateUserCannotWorkMoreThanFiveDaysInARowInTheSameShop(shift);

        return shiftRepository.createShift(shift);
    }

    private void validateShiftCreationNotInThePast(final Shift shift) {
        Instant now = Instant.now();
        if (shift.activeFrom().isBefore(now) || shift.activeFrom().equals(now)){
            throw new ShiftCreationInThePastException();
        }

    }

    private void validateUserIsNotWorkingAtTheSameTime(final Shift shift) {
        List<Shift> shifts = shiftRepository.findShiftByUserIdAndOverlappingDuration(shift.userId(), shift.activeFrom(), shift.activeTo());
        for (Shift existingShift : shifts) {
            if (shift.activeFrom().isBefore(existingShift.activeTo()) || shift.activeFrom().equals(existingShift.activeTo()) ) {
                throw new ActiveShiftException("Shift overlaps with existing shift");
            }
        }
    }

    private void validateUserHoursInTheSameShopWithin24HourWindow(final Shift shift) {
        Instant startOf24hrWindow = shift.activeFrom().minus(Duration.ofHours(24));
        Instant endOf24hrWindow = shift.activeFrom();

        List<Shift> shifts = shiftRepository.findShiftByUserIdAndShopIdWithin(shift.userId(), shift.shopId(), startOf24hrWindow, endOf24hrWindow);
        Long totalDurationInMs = 0L;
        for (Shift existingShift : shifts) {
            totalDurationInMs = totalDurationInMs + existingShift.calculateDurationInMs();
        }

        if (Duration.ofMillis(totalDurationInMs).toHours() >= 8L){
            throw new MaxHoursInTheSameShopWithin24HourWindowExceededException();
        }

    }

    private void validateUserCannotWorkMoreThanFiveDaysInARowInTheSameShop(final Shift shift) {
        Instant startOf5DayWindow = shift.getStartOf5DayWindow();
        Instant endOf5DayWindow = shift.activeTo();

        List<Shift> shifts = shiftRepository.findShiftByUserIdAndShopIdWithin(shift.userId(), shift.shopId(), startOf5DayWindow, endOf5DayWindow);

        long consecutiveDayCount = 0;
        LocalDate expectedDate = LocalDate.ofInstant(startOf5DayWindow, ZoneId.of(UTC));

        for (Shift existingShift : shifts) {
            LocalDate existingShiftDate = LocalDate.ofInstant(existingShift.activeFrom(), ZoneId.of(UTC));

            if (expectedDate.equals(existingShiftDate)){
                consecutiveDayCount++;
                expectedDate = expectedDate.plusDays(1); // expected date is now the next date
            }
        }

        LocalDate newShiftDate = LocalDate.ofInstant(shift.activeFrom(),ZoneId.of(UTC));
        if (expectedDate.equals(newShiftDate)){
            consecutiveDayCount++; // new shift falls on the next day
        }

        if (consecutiveDayCount > 5L){
            throw new MoreThanFiveDaysInARowInTheSameShopException();
        }
    }
}
