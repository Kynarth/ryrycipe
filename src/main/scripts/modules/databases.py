class DatabasesRes:
    ryrycipe_en = '../resources/databases/ryrycipe_en.db'
    ryrycipe_fr = '../resources/databases/ryrycipe_fr.db'

    @classmethod
    def get(cls, string):
        return cls.__getattribute__(cls, string)