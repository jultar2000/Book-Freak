package com.example.Usermodule.service;

import com.example.Usermodule.dao.AddressDao;
import com.example.Usermodule.dto.AddressDto;
import com.example.Usermodule.entity.Address;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AddressService {

    private AddressDao addressDao;

    private UserService userService;

    private ObjectId convertStringIdToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            log.error("Cannot create ObjectId: {}", e.getMessage());
            String errorMessage = MessageFormat
                    .format("String Id `{0}` cannot be converted to ObjectId.", id);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public boolean insertAddress(Address address, String username) {
        address.setUser(userService.findUser(username));
        return addressDao.insertAddress(address);
    }

    public boolean deleteAddress(String addressId) {
        return addressDao.deleteAddress(convertStringIdToObjectId(addressId));
    }

    public Address findAddress(String addressId) {
        return addressDao.findAddress(convertStringIdToObjectId(addressId));
    }

    public List<Address> findAllAddresses() {
        return addressDao.findAllAddresses();
    }

    public boolean updateAddressFields(String addressId, AddressDto request) {
        return addressDao.updateAddressFields(convertStringIdToObjectId(addressId),
                request.getCountry(),
                request.getCity(),
                request.getStreet(),
                request.getHouseNumber(),
                request.getZip(),
                request.isDefaultChoice(),
                request.getAddressType());

    }
}
