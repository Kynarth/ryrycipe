#!/usr/bin/python3

"""Populate french database."""

import sys
import json

from modules.json import JsonRes
from modules.databases import DatabasesRes
from modules.dbmanager import DatabaseManager


# pylint: disable-msg=C0103
dbm = DatabaseManager(DatabasesRes.ryrycipe_fr)

QUALITY_LIST = [
    'Base', 'Fine', 'Fin', 'Choix', 'Excellente', 'Excellent', 'Suprême'
]

GENRE_TO_QUALITY = {
    'Excellent': 'Excellente', 'Fin': 'Fine', 'Base': 'Base', 'Fine': 'Fine',
    'Choix': 'Choix', 'Excellente': 'Excellente', 'Suprême': 'Suprême'
}

LOC_TO_FACTION = {
    'Forêts': "Matis", 'Jungle': 'Zoraï', 'Lacs': 'Tryker',
    'Désert': 'Fyros', 'P. Racines': 'Primes', 'Racines': 'Primes'
}

QUALITIES = ['Base', 'Fine', 'Choix', 'Excellente', 'Suprême']

FACTIONS = {
    'Matis': 'BK_matis.png',
    'Fyros': 'BK_fyros.png',
    'Tryker': 'BK_tryker.png',
    'Zoraï': 'BK_zoraï.png',
    'Primes': 'BK_primes.png'
}


def get_data():
    """Construct a dictionary with materials informations.

    Returns:
        dict - A dictionary with material category infos for each basic
        material code.

    Example:
        >>> get_data()['m0568']
        {'name': 'Izam', 'category': 'Quartered', 'type': 'Ligament'}
    """
    data = {}

    with open(JsonRes.materials_fr, 'r', encoding='UTF-8') as f_mat:
        materials = json.load(f_mat)

    for categories in materials.values():
        for category, types in categories.items():
            for material_type, names in types.items():
                for name, code in names.items():
                    data[code] = {"category": category, 'type': material_type,
                                  "name": name}

    return data


def populate_material_spec_table():
    """Populate 'material_spec' table."""
    dbm.query(
        "INSERT INTO material_spec(quality, faction) "
        "VALUES('Base', 'Générique')"
    )
    dbm.query(
        "INSERT INTO material_spec(quality, faction) "
        "VALUES('Fine', 'Générique')"
    )

    for quality in QUALITIES:
        if quality not in ['Base', 'Fine']:
            for faction in FACTIONS.keys():
                dbm.query(
                    "INSERT INTO material_spec(quality, faction) "
                    "VALUES(?, ?)",
                    (quality, faction)
                )


def populate_component_table():
    """Populate 'component' table."""
    with open(JsonRes.category_to_icon_fr, 'r', encoding="UTF-8") as f_mats:
        materials = json.load(f_mats)

    for code, values in materials.items():

        dbm.query(
            """INSERT INTO component VALUES(?, ?, ?)""",
            (code, values['name'], values['icon'] + ".png")
        )


def populate_faction_table():
    """Populate 'faction' table."""

    # Add generic faction
    dbm.query(
        """INSERT INTO faction VALUES(?, ?)""",
        ("Générique", "BK_generic.png")
    )

    for faction_name, faction_icon in FACTIONS.items():
        dbm.query(
            """INSERT INTO faction VALUES(?, ?)""",
            (faction_name, faction_icon)
        )


def get_quality(description):
    """With material description, extract it's quality.

    Args:
        description (str): A material description.

    Returns:
        str - The extracted quality from description.

    Example:
        >>> get_quality("Bundle of Basic Abhaya Wood")
        Basic

    """
    try:
        return GENRE_TO_QUALITY[description.split('/')[0].split()[-1]]
    except IndexError:
        print("IndexError quality:", description, file=sys.stderr)
        sys.exit()
    except KeyError:
        # Quality not is the expected place in the description
        for quality in QUALITY_LIST:
            if quality in description:
                return GENRE_TO_QUALITY[quality]


def get_faction(description):
    """With material description, extract its faction.

    Args:
        description (str): A material description.

    Returns:
        str - The extracted faction from description.

    Example:
        >>> get_faction("Fagot de Base/Bois d'Abhaya")
        None

        >>> get_faction("Fragment d'Epine Excellente/Arma des Forêts")
        Matis

    """
    try:
        return LOC_TO_FACTION[(description.split('/')[1].split()[-1])]
    except KeyError:
        return None


def get_component_codes(category, mat_type, mat_name):
    """Return the two components codes for a material category.

    Args:
        category (str): Material category -> "Foraged" or "Quartered".
        mat_type (str): Material type like Amber, Shell, Spine etc...
        mat_name (str): Material name like Beng, Cuty, Arma etc...

    Returns:
        list - A list with the two searched component id.

    Example:
        >>> get_component_codes("Quartered", "Rostrum", "Kipucka")
        ['mpftMpAT', 'mpftMpPE']

    """
    with open(JsonRes.materials_fr, 'r', encoding='UTF-8') as f_mats:
        materials = json.load(f_mats)

    codes = []
    for code, values in materials.items():
        comp_code = None

        try:
            comp_code = values[category][mat_type][mat_name]
        except KeyError:
            pass

        if comp_code:
            codes.append(code)

    return codes


def get_mat_type_id(type_name):
    """Get the material category type id in function of his name.

    Args:
        type_name (str): Material category type name like Wood, Shell etc...

    Returns:
        int - The id of the corresponding material_category_type.

    Example:
        >>> get_cat_type_id("Wood")
        23

    """
    dbm.query(
        "SELECT id FROM material_category_type WHERE name = ?", (type_name, )
    )

    try:
        return dbm.cur.fetchone()[0]
    except TypeError:
        return None


def get_mat_type_icon(type_name):
    """Return the icon corresponding to the material type.

    Args:
        type_name (str): Material category type name like Wood, Shell etc...

    Returns:
        str - The path to the searched icon.

    """
    with open(JsonRes.material_to_icon_fr, 'r', encoding='UTF-8') as f_icons:
        icons = json.load(f_icons)

    return icons.get(type_name, None)


def get_cat_id(category, mat_type, mat_name):
    """Get the material category id in function of category, type and name.

    Args:
        category (str): Material category -> "Foraged" or "Quartered".
        mat_type (str): Material type like Amber, Shell, Spine etc...
        mat_name (str): Material name like Beng, Cuty, Arma etc...

    Returns:
        int - The id of corresponding material_category.

    Example:
        >>> get_cat_id("Foraged", "Wood", "Abhaya")
        1

    """
    dbm.query(
        "SELECT id FROM material_category "
        "WHERE category = ? AND name = ? AND type_id = ("
        "SELECT id FROM material_category_type "
        "WHERE name = ?)",
        (category, mat_name, mat_type)
    )
    try:
        return dbm.cur.fetchone()[0]
    except TypeError:
        return None


def get_spec_id(mat_quality, mat_faction=None):
    """
    Get the material_spec id corresponding to the material quality and faction.

    Args:
        mat_quality (str): A material quality like Basic, Fine, Choice etc...
        mat_faction (str): A material faction like Matis, Zoraï etc...

    Returns:
        int - The id of the corresponding material_spec.

    Example:
        >>> get_spec_id('Basic', 'Generic')
        1

    """
    if mat_faction:
        dbm.query(
            "SELECT id FROM material_spec WHERE quality = ? AND faction = ?",
            (mat_quality, mat_faction)
        )
    else:
        dbm.query(
            "SELECT id FROM material_spec WHERE quality = ?",
            (mat_quality,)
        )

    try:
        return dbm.cur.fetchone()[0]
    except TypeError:
        print(
            "Wrong quality: {} or faction: {}".format(
                mat_quality, mat_faction),
            file=sys.stderr
        )
        sys.exit()


def get_mat_comp_id(first_comp, sec_comp):
    """Get id for a pair of material component.

    Args:
        first_comp (str): First component id from a material. Ex: mpftMpCA
        sec_comp (str): Second component id from a material. Ex: mpftMpCR

    Returns:
        int - The id of corresponding material_component.

    """
    dbm.query(
        "SELECT id FROM material_component "
        "WHERE component_id_1 = ? OR component_id_1 = ?",
        (first_comp, sec_comp)
    )
    try:
        return dbm.cur.fetchone()[0]
    except TypeError:
        return None


def insert_type(type_name):
    """Insert a material category type in the 'material_category_type' table."""

    # Check if the material type already exists
    if get_mat_type_id(type_name):
        return

    dbm.query(
        "INSERT INTO material_category_type(name, icon) VALUES(?, ?)",
        (type_name, get_mat_type_icon(type_name) + ".png")
    )


def insert_category(category, mat_type, mat_name, comps_id):
    """Insert a category in the 'material_category' table.

    Args:
        category (str): Material category -> "Foraged" or "Quartered".
        mat_type (str): Material type like Amber, Shell, Spine etc...
        mat_name (str): Material name like Beng, Cuty, Arma etc...
        comps_id (int): Id referring to material components.

    """
    # Check if the category already exists
    cat_id = get_cat_id(category, mat_type, mat_name)

    if cat_id:
        return cat_id

    type_id = get_mat_type_id(mat_type)

    dbm.query(
        "INSERT INTO material_category"
        "(category, type_id, name, material_component_id) "
        "VALUES(?, ?, ?, ?)",
        (category, type_id, mat_name, comps_id)
    )

    dbm.query("SELECT id FROM material_category ORDER BY id DESC LIMIT 1")

    return dbm.cur.fetchone()[0]


def insert_components(first_comp, sec_comp):
    """Insert components codes in 'material_component' table.

    Args:
        first_comp (str): First component id from a material. Ex: mpftMpCA
        sec_comp (str): Second component id from a material. Ex: mpftMpCR

    """
    mat_comp_id = get_mat_comp_id(first_comp, sec_comp)

    if mat_comp_id:
        return mat_comp_id

    dbm.query(
        "INSERT INTO material_component(component_id_1, component_id_2) "
        "VALUES(?, ?)", (first_comp, sec_comp)
    )

    dbm.query("SELECT id FROM material_component ORDER BY id DESC LIMIT 1")

    return dbm.cur.fetchone()[0]


def insert_material(mat_id, desc, cat_id, spec_id):
    """Insert a material in the material table.

    Args:
        mat_id (str): Material code like m0001dxacb01.
        cat_id (int): Id referring the material category.
        spec_id (int) Id referring the material quality and faction.

    """
    dbm.query(
        "INSERT INTO material(id, description, category_id, spec_id) "
        "VALUES(?, ?, ?, ?)", (mat_id, desc, cat_id, spec_id)
    )


def construct_database():
    """Construct tables and populate some of them."""
    with open('ryrycipe.sql', 'r', encoding='UTF-8') as f_sql:
        construct_query = f_sql.read()

    dbm.cur.executescript(construct_query)

    populate_material_spec_table()
    populate_component_table()
    populate_faction_table()


def populate_material_tables():
    """Populate tables about materials."""

    # Get the list of all materials
    with open(JsonRes.items_fr, 'r', encoding="UTF-8") as f_items:
        items = json.load(f_items)

    # Get informations about each materials
    data = get_data()

    # Populate material tables
    for item, values in sorted(items.items(), key=lambda key: key):
        description = values['name'].replace("\\", "").replace('P.', 'Primes')

        # It's not a material
        if "/" not in description:
            continue

        code = item[:5]

        try:
            material_type = data[code]['type']
            category = data[code]['category']
            name = data[code]['name']
            codes = get_component_codes(category, material_type, name)
        except KeyError:
            continue

        quality = get_quality(description)
        faction = get_faction(description)

        # Check if the category already exists
        components_id = insert_components(*codes)
        insert_type(material_type)
        category_id = insert_category(
            category, material_type, name, components_id
        )
        insert_material(
            item, description, category_id, get_spec_id(quality, faction)
        )


def flatten_nested_dict(nested_dict):
    """Flatten a nested dict with the keypath as key.

    Args:
        nested_dict (dict): Nested dict to flatten.

    Returns:
        dict - A dict with keypath as key and plan informations in dict form as
        value.

    Example:
        >>> flatten_nested_dict(dictionary)
        {"Arme|Mélée|Arme à une main|Dague":
            {
                'name': 'Dague', 'icon': 'MW_dagger',
                'code': 'bccmea06', 'type': 'Arme 1M'
            }
        }, ...


    """
    def items():
        """itemize dict's leaf."""
        for key, value in nested_dict.items():
            if isinstance(value, dict) and not value.get('icon', None):
                for subkey, subvalue in flatten_nested_dict(value).items():
                    yield key + "|" + subkey, subvalue
            else:
                yield key, value

    return dict(items())


def get_plan(flatten_dict, recipe_code):
    """
    Return the plan from flatten plan dictionary in function of the
    given code.

    Args:
        recipe_code (str): A recipe code like: bccmea05

    Returns:
        tuple - A tuple with keypath and plan infos if the code is found.
        None - Returns None if the code isn't found.

    """
    for key, value in flatten_dict.items():
        if value["code"] == recipe_code:
            return key, value

    return None


def get_recipe_cat_id(category, subcat=None, hand=None):
    """Get the corresponding id for given informations.

    Args:
        category (str): Recipe category like Weapon or Armor etc...
        subcat (str, optional): Recipe subcategory like Melee or
        Medium Armor etc... Not specified for jewels.
        hand (int, optional): Number of hand needed to use the crafted item.
        Only specified for weapons.

    Returns:
        int - Returns the corresponding id from recipe_cateogory table.

    """
    dbm.query(
        "SELECT id FROM recipe_category "
        "WHERE category = ? AND subcategory IS ? AND hand IS ?",
        (category, subcat, hand)
    )
    try:
        return dbm.cur.fetchone()[0]
    except TypeError:
        return None


def insert_recipe_category(category, subcat=None, hand=None):
    """Insert a recipe category in 'recipe_category' table.

    Args:
        category (str): Recipe category like Weapon or Armor etc...
        subcat (str, optional): Recipe subcategory like Melee or
        Medium Armor etc... Not specified for jewels.
        hand (int, optional): Number of hand needed to use the crafted item.
        Only specified for weapons.

    Returns:
        int - Returns the id of the existed or created recipe_category.

    """
    recipe_cat_id = get_recipe_cat_id(category, subcat, hand)
    # If recipe_category already exists returns his id.
    if recipe_cat_id:
        return recipe_cat_id

    dbm.query(
        "INSERT INTO recipe_category(category, subcategory, hand) "
        "VALUES(?, ?, ?)", (category, subcat, hand)
    )

    dbm.query("SELECT id FROM recipe_category ORDER BY id DESC LIMIT 1")

    return dbm.cur.fetchone()[0]


def get_recipe_id(name, quality, category):
    """Get the corresponding id for given informations.

    Args:
        name (str): The name of the recipe like 'Dagger'.
        quality (str): The recipe quality (normal, medium, high).
        category (int): Id for the corresponding recipe_category

    Returns:
        int - Returns the corresponding id from recipe table.

    """
    dbm.query(
        "SELECT id FROM recipe "
        "WHERE name = ? AND quality = ? AND category_id = ?",
        (name, quality, category)
    )
    try:
        return dbm.cur.fetchone()[0]
    except TypeError:
        return None


def insert_recipe(name, quality, icon, category):
    """Insert a recipe in the "recipe" table.

    Args:
        name (str): The plan's name. Ex: 'Dagger'.
        quality (str): The plan's quality (normal, medium and high).
        icon (str): The plan's icon name.
        category (int): Id for the corresponding recipe_category.

    """
    recipe_id = get_recipe_id(name, quality, category)

    if recipe_id:
        return "stop"

    dbm.query(
        "INSERT INTO recipe(name, quality, icon, category_id) "
        "VALUES(?, ?, ?, ?)", (name, quality, icon, category)
    )

    dbm.query("SELECT id FROM recipe ORDER BY id DESC LIMIT 1")

    return dbm.cur.fetchone()[0]


def insert_recipe_component(recipe_id, component, amount):
    """Insert a recipe component in the 'recipe_component' table.

    Args:
        recipe_id (int): The recipe id in recipe table.
        component (str): A component id like: mpftMpGA.
        amount (int): The amount of component needed for the recipe.

    """
    dbm.query(
        "INSERT INTO recipe_component(recipe_id, component_id, amount) "
        "VALUES(?, ?, ?)", (recipe_id, component, amount)
    )


def get_plan_quality(plan_code):
    """Define the quality of plan in function of its code.

    Args:
        plan_code (str): The plan's code like "bcmaea15_3".

    Return:
        quality (str): The plan's quality.

    """
    if plan_code[-2:] == "_2":
        quality = "Moyenne"
    elif plan_code[-2:] == "_3":
        quality = "Haute"
    else:
        quality = "Normale"

    return quality


def populate_recipe_tables():  # pylint: disable=too-many-locals
    """Populate tables about recipes."""
    # Get all plans and recipes
    with open(JsonRes.craftplan, 'r', encoding='UTF-8') as f_recipes:
        recipes = json.load(f_recipes)

    with open(JsonRes.plans_fr, 'r', encoding='UTF-8') as f_plans:
        plans = flatten_nested_dict(json.load(f_plans))

    for recipe, component in recipes.items():
        # filter matis affiliadted recipe to get higher quality recipe
        if recipe[:3] == 'bcf':
            recipe = recipe.replace('bcf', 'bcc')

        # Filter recipes to keep only those are not affiliated to a faction
        if recipe[:3] != 'bcc' and recipe[:4] != 'bcmj':
            continue

        quality = get_plan_quality(recipe)

        try:
            # Split the recipe code to separate the quality code from the rest
            # pylint: disable=unpacking-non-sequence
            keypath, infos = get_plan(plans, recipe.split('_')[0])
        except TypeError:
            print(
                "The recipe code: {} was not found in the ".format(recipe) +
                "flatten dict of plans.",
                file=sys.stderr
            )
            continue

        *categories, recipe_name = keypath.split("|")

        if "Munitions" in recipe_name:
            recipe_name += " de " + categories[-1].lower()
            categories[-1] = None

        if categories[0] == "Arme":
            if "une" in categories[-1]:
                categories[-1] = 1
            elif "deux" in categories[-1] or "Corps à corps" in categories[-1]:
                categories[-1] = 2
            else:
                categories[-1] = None

        recipe_cat_id = insert_recipe_category(*categories)

        recipe_id = insert_recipe(
            recipe_name, quality, infos['icon'] + '.png', recipe_cat_id
        )

        # Recipe already exists
        if recipe_id == 'stop':
            continue

        # Loop over plan's component
        for comp, amount in component['mpft'].items():
            insert_recipe_component(recipe_id, comp, amount)


if __name__ == '__main__':
    construct_database()
    print("Database constructed")
    populate_material_tables()
    print("Material tables populated")
    populate_recipe_tables()
    print("Recipe tables populated")
