package test.tencent.com.offdemo.util;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by hoollyzhang on 16/9/28.
 * Description :
 */

public class MyRealmMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();


        // Migrate to version 2: Add a primary key + object references
        // Example:
        // public Person extends RealmObject {
        //     private String name;
        //     @PrimaryKey
        //     private int age;
        //     private Dog favoriteDog;
        //     private RealmList<Dog> dogs;
        //     // getters and setters left out for brevity
        // }
        if (oldVersion == 1) {
            schema.get("Post")
                    .addField("ctime", long.class);
                    //.addRealmObjectField("favoriteDog", schema.get("Dog"))
                    //.addRealmListField("dogs", schema.get("Dog"));
            oldVersion++;
        }
    }
}
