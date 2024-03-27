package com.example.workshift.business.shift;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
class ShiftServiceImpl implements ShiftService {

    private ShiftRepository shiftRepository;

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
        //TODO: Validate the shift to be created is not in the past
    }

    private void validateUserIsNotWorkingAtTheSameTime(final Shift shift) {
        //TODO: Validate the user is not working at the same time as the shift to be created
    }

    private void validateUserHoursInTheSameShopWithin24HourWindow(final Shift shift) {
        //TODO: Validate the user hours worked in the same shop within 24hours
    }

    private void validateUserCannotWorkMoreThanFiveDaysInARowInTheSameShop(final Shift shift) {
        //TODO: Validate the user cannot work more than five days in a row in the same shop
    }
}
