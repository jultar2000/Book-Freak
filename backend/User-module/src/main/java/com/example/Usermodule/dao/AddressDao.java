package com.example.Usermodule.dao;

import com.example.Usermodule.entity.Address;
import com.example.Usermodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@Slf4j
public class AddressDao {

    private static final String ADDRESS_COLLECTION = "addresses";

    private final MongoCollection<Address> addressCollection;

    @Autowired
    public AddressDao(MongoClient mongoClient,
                   @Value("${spring.data.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.addressCollection =
                database.getCollection(ADDRESS_COLLECTION, Address.class).withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Inserts the 'address' object in the 'addresses' collection.
     *
     * @param address - Address object to be inserted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean insertAddress(Address address) {
        try {
            addressCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(address);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'addresses' collection: {}", address.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Address with Id `{0}` already exists.", address.getOid()));
        }
    }

    /**
     * Deletes the address document from the 'addresses' collection with the provided addressId.
     *
     * @param addressId - id of the address to be deleted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean deleteAddress(ObjectId addressId) {
        Bson find_query = Filters.in("_id", addressId);
        try {
            DeleteResult result = addressCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'addresses' collection. No address deleted.", addressId);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'addresses' collection: {1}.", addressId, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    /**
     * Given the addressId, finds the address object in 'addresses' collection.
     *
     * @param addressId - id of the address.
     * @return address object, if null throws IncorrectDaoOperation.
     */
    public Address findAddress(ObjectId addressId) {
        Bson find_query = Filters.in("_id", addressId);
        Address address = addressCollection.find(find_query).first();
        if (address == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Address with Id `{0}` does not exist.", addressId));
        }
        return address;
    }

    /**
     * Finds all addresses in 'addresses' collection.
     *
     * @return list of found addresses.
     */
    public List<Address> findAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        addressCollection
                .find()
                .into(addresses);
        return addresses;
    }

    public boolean updateAddressFields(ObjectId addressId,
                                       String country,
                                       String city,
                                       String street,
                                       String houseNumber,
                                       String zip,
                                       boolean defaultChoice,
                                       String addressType) {
        Bson find_query = in("_id", addressId);
        List<Bson> updatesList = new ArrayList<>();
        if (country != null) {
            updatesList.add(Updates.set("country", country));
        }
        if (city != null) {
            updatesList.add(Updates.set("city", city));
        }
        if (street != null) {
            updatesList.add(Updates.set("street", street));
        }
        if (houseNumber != null) {
            updatesList.add(Updates.set("houseNumber", houseNumber));
        }
        if (zip != null) {
            updatesList.add(Updates.set("zip", zip));
        }
        updatesList.add(Updates.set("defaultChoice", defaultChoice));
        if (addressType != null) {
            updatesList.add(Updates.set("addressType", addressType));
        }

        Bson update = Updates.combine(updatesList);
        return performUpdate(addressId.toString(), find_query, update);
    }

    private boolean performUpdate(String addressId, Bson find_query, Bson update) {
        try {
            UpdateResult updateResult = addressCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "Address `{}` was not updated. Address might not exist or all fields remain the same.",
                        addressId);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update address `{0}`: {1}",
                            addressId,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }

}

