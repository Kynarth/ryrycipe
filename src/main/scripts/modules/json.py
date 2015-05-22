class JsonRes:
    craftplan = 'json/craftplan.json'
    category_to_icon_en = 'json/en/category_to_icon_en.json'
    words_en = 'json/en/words_en.json'
    items_en = 'json/en/items_en.json'
    material_to_icon_en = 'json/en/material_to_icon_en.json'
    plans_en = 'json/en/plans_en.json'
    materials_en = 'json/en/materials_en.json'
    items_fr = 'json/fr/items_fr.json'
    materials_fr = 'json/fr/materials_fr.json'
    words_fr = 'json/fr/words_fr.json'
    plans_fr = 'json/fr/plans_fr.json'
    category_to_icon_fr = 'json/fr/category_to_icon_fr.json'
    material_to_icon_fr = 'json/fr/material_to_icon_fr.json'

    @classmethod
    def get(cls, string):
        return cls.__getattribute__(cls, string)