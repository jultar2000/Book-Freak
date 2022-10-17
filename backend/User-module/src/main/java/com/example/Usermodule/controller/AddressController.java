package com.example.Usermodule.controller;

import com.example.Usermodule.dto.GetAddressDto;
import com.example.Usermodule.dto.AddressDto;
import com.example.Usermodule.entity.Address;
import com.example.Usermodule.service.AddressService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private final ModelMapper mapper;

    @GetMapping("/{addressId}")
    public ResponseEntity<GetAddressDto> getAddress(@PathVariable("addressId") String addressId) {
        Address address = addressService.findAddress(addressId);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.map(address, GetAddressDto.class));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAddressDto>> getAddresses() {
        List<GetAddressDto> addressDtos =
                addressService
                        .findAllAddresses()
                        .stream()
                        .map(address ->
                                mapper.map(address, GetAddressDto.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(addressDtos);
    }

    @PostMapping("/{username}")
    public ResponseEntity<Void> addAddress(@PathVariable("username") String username,
                                           @RequestBody AddressDto request) {
        Address address = mapper.map(request, Address.class);
        if (!addressService.insertAddress(address, username)) {
            return ResponseEntity.badRequest().build();
        }
//        bookModuleEventClient.insertAuthor(author.getOid());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Void> updateAddress(@PathVariable("addressId") String addressId,
                                              @RequestBody AddressDto request) {
        if (!addressService.updateAddressFields(addressId, request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") String addressId) {
        if (!addressService.deleteAddress(addressId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }

}
