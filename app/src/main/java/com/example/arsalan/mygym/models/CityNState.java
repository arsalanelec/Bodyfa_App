package com.example.arsalan.mygym.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsalan on 19-10-2017.
 */

public class CityNState {
    private static final String cityJson = "[{\"id\":1,\"name\":\"آذربایجان شرقی\",\"cities\":[{\"id\":1,\"province_id\":1,\"name\":\"آذر شهر\"},\n" +
            "{\"id\":2,\"province_id\":1,\"name\":\"اسكو\"},\n" +
            "{\"id\":3,\"province_id\":1,\"name\":\"اهر\"},\n" +
            "{\"id\":4,\"province_id\":1,\"name\":\"بستان آباد\"},\n" +
            "{\"id\":5,\"province_id\":1,\"name\":\"بناب\"},\n" +
            "{\"id\":6,\"province_id\":1,\"name\":\"بندر شرفخانه\"},\n" +
            "{\"id\":7,\"province_id\":1,\"name\":\"تبریز\"},\n" +
            "{\"id\":8,\"province_id\":1,\"name\":\"تسوج\"},\n" +
            "{\"id\":9,\"province_id\":1,\"name\":\"جلفا\"},\n" +
            "{\"id\":10,\"province_id\":1,\"name\":\"سراب\"},\n" +
            "{\"id\":11,\"province_id\":1,\"name\":\"شبستر\"},\n" +
            "{\"id\":12,\"province_id\":1,\"name\":\"صوفیان\"},\n" +
            "{\"id\":13,\"province_id\":1,\"name\":\"عجبشیر\"},\n" +
            "{\"id\":14,\"province_id\":1,\"name\":\"قره آغاج\"},\n" +
            "{\"id\":15,\"province_id\":1,\"name\":\"كلیبر\"},\n" +
            "{\"id\":16,\"province_id\":1,\"name\":\"كندوان\"},\n" +
            "{\"id\":17,\"province_id\":1,\"name\":\"مراغه\"},\n" +
            "{\"id\":18,\"province_id\":1,\"name\":\"مرند\"},\n" +
            "{\"id\":19,\"province_id\":1,\"name\":\"ملكان\"},\n" +
            "{\"id\":20,\"province_id\":1,\"name\":\"ممقان\"},\n" +
            "{\"id\":21,\"province_id\":1,\"name\":\"میانه\"},\n" +
            "{\"id\":22,\"province_id\":1,\"name\":\"هادیشهر\"},\n" +
            "{\"id\":23,\"province_id\":1,\"name\":\"هریس\"},\n" +
            "{\"id\":24,\"province_id\":1,\"name\":\"هشترود\"},\n" +
            "{\"id\":25,\"province_id\":1,\"name\":\"ورزقان\"}]},\n" +
            "{\"id\":2,\"name\":\"آذربایجان غربی\",\"cities\":[{\"id\":26,\"province_id\":2,\"name\":\"ارومیه\"},\n" +
            "{\"id\":27,\"province_id\":2,\"name\":\"اشنویه\"},\n" +
            "{\"id\":28,\"province_id\":2,\"name\":\"بازرگان\"},\n" +
            "{\"id\":29,\"province_id\":2,\"name\":\"بوكان\"},\n" +
            "{\"id\":30,\"province_id\":2,\"name\":\"پلدشت\"},\n" +
            "{\"id\":31,\"province_id\":2,\"name\":\"پیرانشهر\"},\n" +
            "{\"id\":32,\"province_id\":2,\"name\":\"تكاب\"},\n" +
            "{\"id\":33,\"province_id\":2,\"name\":\"خوی\"},\n" +
            "{\"id\":34,\"province_id\":2,\"name\":\"سردشت\"},\n" +
            "{\"id\":35,\"province_id\":2,\"name\":\"سلماس\"},\n" +
            "{\"id\":36,\"province_id\":2,\"name\":\"سیه چشمه- چالدران\"},\n" +
            "{\"id\":37,\"province_id\":2,\"name\":\"سیمینه\"},\n" +
            "{\"id\":38,\"province_id\":2,\"name\":\"شاهین دژ\"},\n" +
            "{\"id\":39,\"province_id\":2,\"name\":\"شوط\"},\n" +
            "{\"id\":40,\"province_id\":2,\"name\":\"ماكو\"},\n" +
            "{\"id\":41,\"province_id\":2,\"name\":\"مهاباد\"},\n" +
            "{\"id\":42,\"province_id\":2,\"name\":\"میاندوآب\"},\n" +
            "{\"id\":43,\"province_id\":2,\"name\":\"نقده\"}]},\n" +
            "{\"id\":3,\"name\":\"اردبیل\",\"cities\":[{\"id\":44,\"province_id\":3,\"name\":\"اردبیل\"},\n" +
            "{\"id\":45,\"province_id\":3,\"name\":\"بیله سوار\"},\n" +
            "{\"id\":46,\"province_id\":3,\"name\":\"پارس آباد\"},\n" +
            "{\"id\":47,\"province_id\":3,\"name\":\"خلخال\"},\n" +
            "{\"id\":48,\"province_id\":3,\"name\":\"سرعین\"},\n" +
            "{\"id\":49,\"province_id\":3,\"name\":\"كیوی(كوثر)\"},\n" +
            "{\"id\":50,\"province_id\":3,\"name\":\"گرمی(مغان)\"},\n" +
            "{\"id\":51,\"province_id\":3,\"name\":\"مشگین شهر\"},\n" +
            "{\"id\":52,\"province_id\":3,\"name\":\"مغان(سمنان)\"},\n" +
            "{\"id\":53,\"province_id\":3,\"name\":\"نمین\"},\n" +
            "{\"id\":54,\"province_id\":3,\"name\":\"نیر\"}]},\n" +
            "{\"id\":4,\"name\":\"اصفهان\",\"cities\":[{\"id\":55,\"province_id\":4,\"name\":\"آران و بیدگل\"},\n" +
            "{\"id\":56,\"province_id\":4,\"name\":\"اردستان\"},\n" +
            "{\"id\":57,\"province_id\":4,\"name\":\"اصفهان\"},\n" +
            "{\"id\":58,\"province_id\":4,\"name\":\"باغ بهادران\"},\n" +
            "{\"id\":59,\"province_id\":4,\"name\":\"تیران\"},\n" +
            "{\"id\":60,\"province_id\":4,\"name\":\"خمینی شهر\"},\n" +
            "{\"id\":61,\"province_id\":4,\"name\":\"خوانسار\"},\n" +
            "{\"id\":62,\"province_id\":4,\"name\":\"دهاقان\"},\n" +
            "{\"id\":63,\"province_id\":4,\"name\":\"دولت آباد-اصفهان\"},\n" +
            "{\"id\":64,\"province_id\":4,\"name\":\"زرین شهر\"},\n" +
            "{\"id\":65,\"province_id\":4,\"name\":\"زیباشهر(محمدیه)\"},\n" +
            "{\"id\":66,\"province_id\":4,\"name\":\"سمیرم\"},\n" +
            "{\"id\":67,\"province_id\":4,\"name\":\"شاهین شهر\"},\n" +
            "{\"id\":68,\"province_id\":4,\"name\":\"شهرضا\"},\n" +
            "{\"id\":69,\"province_id\":4,\"name\":\"فریدن\"},\n" +
            "{\"id\":70,\"province_id\":4,\"name\":\"فریدون شهر\"},\n" +
            "{\"id\":71,\"province_id\":4,\"name\":\"فلاورجان\"},\n" +
            "{\"id\":72,\"province_id\":4,\"name\":\"فولاد شهر\"},\n" +
            "{\"id\":73,\"province_id\":4,\"name\":\"قهدریجان\"},\n" +
            "{\"id\":74,\"province_id\":4,\"name\":\"كاشان\"},\n" +
            "{\"id\":75,\"province_id\":4,\"name\":\"گلپایگان\"},\n" +
            "{\"id\":76,\"province_id\":4,\"name\":\"گلدشت اصفهان\"},\n" +
            "{\"id\":77,\"province_id\":4,\"name\":\"گلدشت مركزی\"},\n" +
            "{\"id\":78,\"province_id\":4,\"name\":\"مباركه اصفهان\"},\n" +
            "{\"id\":79,\"province_id\":4,\"name\":\"مهاباد-اصفهان\"},\n" +
            "{\"id\":80,\"province_id\":4,\"name\":\"نایین\"},\n" +
            "{\"id\":81,\"province_id\":4,\"name\":\"نجف آباد\"},\n" +
            "{\"id\":82,\"province_id\":4,\"name\":\"نطنز\"},\n" +
            "{\"id\":83,\"province_id\":4,\"name\":\"هرند\"}]},\n" +
            "{\"id\":5,\"name\":\"البرز\",\"cities\":[{\"id\":84,\"province_id\":5,\"name\":\"آسارا\"},\n" +
            "{\"id\":85,\"province_id\":5,\"name\":\"اشتهارد\"},\n" +
            "{\"id\":86,\"province_id\":5,\"name\":\"شهر جدید هشتگرد\"},\n" +
            "{\"id\":87,\"province_id\":5,\"name\":\"طالقان\"},\n" +
            "{\"id\":88,\"province_id\":5,\"name\":\"كرج\"},\n" +
            "{\"id\":89,\"province_id\":5,\"name\":\"گلستان تهران\"},\n" +
            "{\"id\":90,\"province_id\":5,\"name\":\"نظرآباد\"},\n" +
            "{\"id\":91,\"province_id\":5,\"name\":\"هشتگرد\"}]},\n" +
            "{\"id\":6,\"name\":\"ایلام\",\"cities\":[{\"id\":92,\"province_id\":6,\"name\":\"آبدانان\"},\n" +
            "{\"id\":93,\"province_id\":6,\"name\":\"ایلام\"},\n" +
            "{\"id\":94,\"province_id\":6,\"name\":\"ایوان\"},\n" +
            "{\"id\":95,\"province_id\":6,\"name\":\"دره شهر\"},\n" +
            "{\"id\":96,\"province_id\":6,\"name\":\"دهلران\"},\n" +
            "{\"id\":97,\"province_id\":6,\"name\":\"سرابله\"},\n" +
            "{\"id\":98,\"province_id\":6,\"name\":\"شیروان چرداول\"},\n" +
            "{\"id\":99,\"province_id\":6,\"name\":\"مهران\"}]},\n" +
            "{\"id\":7,\"name\":\"بوشهر\",\"cities\":[{\"id\":100,\"province_id\":7,\"name\":\"آبپخش\"},\n" +
            "{\"id\":101,\"province_id\":7,\"name\":\"اهرم\"},\n" +
            "{\"id\":102,\"province_id\":7,\"name\":\"برازجان\"},\n" +
            "{\"id\":103,\"province_id\":7,\"name\":\"بندر دیر\"},\n" +
            "{\"id\":104,\"province_id\":7,\"name\":\"بندر دیلم\"},\n" +
            "{\"id\":105,\"province_id\":7,\"name\":\"بندر كنگان\"},\n" +
            "{\"id\":106,\"province_id\":7,\"name\":\"بندر گناوه\"},\n" +
            "{\"id\":107,\"province_id\":7,\"name\":\"بوشهر\"},\n" +
            "{\"id\":108,\"province_id\":7,\"name\":\"تنگستان\"},\n" +
            "{\"id\":109,\"province_id\":7,\"name\":\"جزیره خارك\"},\n" +
            "{\"id\":110,\"province_id\":7,\"name\":\"جم(ولایت)\"},\n" +
            "{\"id\":111,\"province_id\":7,\"name\":\"خورموج\"},\n" +
            "{\"id\":112,\"province_id\":7,\"name\":\"دشتستان - شبانکاره\"},\n" +
            "{\"id\":113,\"province_id\":7,\"name\":\"دلوار\"},\n" +
            "{\"id\":114,\"province_id\":7,\"name\":\"عسلویه\"}]},\n" +
            "{\"id\":8,\"name\":\"تهران\",\"cities\":[{\"id\":115,\"province_id\":8,\"name\":\"اسلامشهر\"},\n" +
            "{\"id\":116,\"province_id\":8,\"name\":\"بومهن\"},\n" +
            "{\"id\":117,\"province_id\":8,\"name\":\"پاكدشت\"},\n" +
            "{\"id\":118,\"province_id\":8,\"name\":\"تهران\"},\n" +
            "{\"id\":119,\"province_id\":8,\"name\":\"چهاردانگه\"},\n" +
            "{\"id\":120,\"province_id\":8,\"name\":\"دماوند\"},\n" +
            "{\"id\":121,\"province_id\":8,\"name\":\"رودهن\"},\n" +
            "{\"id\":122,\"province_id\":8,\"name\":\"ری\"},\n" +
            "{\"id\":123,\"province_id\":8,\"name\":\"شریف آباد\"},\n" +
            "{\"id\":124,\"province_id\":8,\"name\":\"شهر رباط كریم\"},\n" +
            "{\"id\":125,\"province_id\":8,\"name\":\"شهر شهریار\"},\n" +
            "{\"id\":126,\"province_id\":8,\"name\":\"فشم\"},\n" +
            "{\"id\":127,\"province_id\":8,\"name\":\"فیروزكوه\"},\n" +
            "{\"id\":128,\"province_id\":8,\"name\":\"قدس\"},\n" +
            "{\"id\":129,\"province_id\":8,\"name\":\"كهریزك\"},\n" +
            "{\"id\":130,\"province_id\":8,\"name\":\"لواسان بزرگ\"},\n" +
            "{\"id\":131,\"province_id\":8,\"name\":\"ملارد\"},\n" +
            "{\"id\":132,\"province_id\":8,\"name\":\"ورامین\"}]},\n" +
            "{\"id\":9,\"name\":\"چهارمحال بختیاری\",\"cities\":[{\"id\":133,\"province_id\":9,\"name\":\"اردل\"},\n" +
            "{\"id\":134,\"province_id\":9,\"name\":\"بروجن\"},\n" +
            "{\"id\":135,\"province_id\":9,\"name\":\"چلگرد(كوهرنگ)\"},\n" +
            "{\"id\":136,\"province_id\":9,\"name\":\"سامان\"},\n" +
            "{\"id\":137,\"province_id\":9,\"name\":\"شهركرد\"},\n" +
            "{\"id\":138,\"province_id\":9,\"name\":\"فارسان\"},\n" +
            "{\"id\":139,\"province_id\":9,\"name\":\"لردگان\"}]},\n" +
            "{\"id\":10,\"name\":\"خراسان جنوبی\",\"cities\":[{\"id\":140,\"province_id\":10,\"name\":\"بشرویه\"},\n" +
            "{\"id\":141,\"province_id\":10,\"name\":\"بیرجند\"},\n" +
            "{\"id\":142,\"province_id\":10,\"name\":\"خضری\"},\n" +
            "{\"id\":143,\"province_id\":10,\"name\":\"خوسف\"},\n" +
            "{\"id\":144,\"province_id\":10,\"name\":\"سرایان\"},\n" +
            "{\"id\":145,\"province_id\":10,\"name\":\"سربیشه\"},\n" +
            "{\"id\":146,\"province_id\":10,\"name\":\"طبس\"},\n" +
            "{\"id\":147,\"province_id\":10,\"name\":\"فردوس\"},\n" +
            "{\"id\":148,\"province_id\":10,\"name\":\"قائن\"},\n" +
            "{\"id\":149,\"province_id\":10,\"name\":\"نهبندان\"}]},\n" +
            "{\"id\":11,\"name\":\"خراسان رضوی\",\"cities\":[{\"id\":150,\"province_id\":11,\"name\":\"بجستان\"},\n" +
            "{\"id\":151,\"province_id\":11,\"name\":\"بردسكن\"},\n" +
            "{\"id\":152,\"province_id\":11,\"name\":\"تایباد\"},\n" +
            "{\"id\":153,\"province_id\":11,\"name\":\"تربت جام\"},\n" +
            "{\"id\":154,\"province_id\":11,\"name\":\"تربت حیدریه\"},\n" +
            "{\"id\":155,\"province_id\":11,\"name\":\"جغتای\"},\n" +
            "{\"id\":156,\"province_id\":11,\"name\":\"جوین\"},\n" +
            "{\"id\":157,\"province_id\":11,\"name\":\"چناران\"},\n" +
            "{\"id\":158,\"province_id\":11,\"name\":\"خلیل آباد\"},\n" +
            "{\"id\":159,\"province_id\":11,\"name\":\"خواف\"},\n" +
            "{\"id\":160,\"province_id\":11,\"name\":\"درگز\"},\n" +
            "{\"id\":161,\"province_id\":11,\"name\":\"رشتخوار\"},\n" +
            "{\"id\":162,\"province_id\":11,\"name\":\"سبزوار\"},\n" +
            "{\"id\":163,\"province_id\":11,\"name\":\"سرخس\"},\n" +
            "{\"id\":164,\"province_id\":11,\"name\":\"طبس\"},\n" +
            "{\"id\":165,\"province_id\":11,\"name\":\"طرقبه\"},\n" +
            "{\"id\":166,\"province_id\":11,\"name\":\"فریمان\"},\n" +
            "{\"id\":167,\"province_id\":11,\"name\":\"قوچان\"},\n" +
            "{\"id\":168,\"province_id\":11,\"name\":\"كاشمر\"},\n" +
            "{\"id\":169,\"province_id\":11,\"name\":\"كلات\"},\n" +
            "{\"id\":170,\"province_id\":11,\"name\":\"گناباد\"},\n" +
            "{\"id\":171,\"province_id\":11,\"name\":\"مشهد\"},\n" +
            "{\"id\":172,\"province_id\":11,\"name\":\"نیشابور\"}]},\n" +
            "{\"id\":12,\"name\":\"خراسان شمالی\",\"cities\":[{\"id\":173,\"province_id\":12,\"name\":\"آشخانه، مانه و سمرقان\"},\n" +
            "{\"id\":174,\"province_id\":12,\"name\":\"اسفراین\"},\n" +
            "{\"id\":175,\"province_id\":12,\"name\":\"بجنورد\"},\n" +
            "{\"id\":176,\"province_id\":12,\"name\":\"جاجرم\"},\n" +
            "{\"id\":177,\"province_id\":12,\"name\":\"شیروان\"},\n" +
            "{\"id\":178,\"province_id\":12,\"name\":\"فاروج\"}]},\n" +
            "{\"id\":13,\"name\":\"خوزستان\",\"cities\":[{\"id\":179,\"province_id\":13,\"name\":\"آبادان\"},\n" +
            "{\"id\":180,\"province_id\":13,\"name\":\"امیدیه\"},\n" +
            "{\"id\":181,\"province_id\":13,\"name\":\"اندیمشك\"},\n" +
            "{\"id\":182,\"province_id\":13,\"name\":\"اهواز\"},\n" +
            "{\"id\":183,\"province_id\":13,\"name\":\"ایذه\"},\n" +
            "{\"id\":184,\"province_id\":13,\"name\":\"باغ ملك\"},\n" +
            "{\"id\":185,\"province_id\":13,\"name\":\"بستان\"},\n" +
            "{\"id\":186,\"province_id\":13,\"name\":\"بندر ماهشهر\"},\n" +
            "{\"id\":187,\"province_id\":13,\"name\":\"بندرامام خمینی\"},\n" +
            "{\"id\":188,\"province_id\":13,\"name\":\"بهبهان\"},\n" +
            "{\"id\":189,\"province_id\":13,\"name\":\"خرمشهر\"},\n" +
            "{\"id\":190,\"province_id\":13,\"name\":\"دزفول\"},\n" +
            "{\"id\":191,\"province_id\":13,\"name\":\"رامشیر\"},\n" +
            "{\"id\":192,\"province_id\":13,\"name\":\"رامهرمز\"},\n" +
            "{\"id\":193,\"province_id\":13,\"name\":\"سوسنگرد\"},\n" +
            "{\"id\":194,\"province_id\":13,\"name\":\"شادگان\"},\n" +
            "{\"id\":195,\"province_id\":13,\"name\":\"شوش\"},\n" +
            "{\"id\":196,\"province_id\":13,\"name\":\"شوشتر\"},\n" +
            "{\"id\":197,\"province_id\":13,\"name\":\"لالی\"},\n" +
            "{\"id\":198,\"province_id\":13,\"name\":\"مسجد سلیمان\"},\n" +
            "{\"id\":199,\"province_id\":13,\"name\":\"هندیجان\"},\n" +
            "{\"id\":200,\"province_id\":13,\"name\":\"هویزه\"}]},\n" +
            "{\"id\":14,\"name\":\"زنجان\",\"cities\":[{\"id\":201,\"province_id\":14,\"name\":\"آب بر(طارم)\"},\n" +
            "{\"id\":202,\"province_id\":14,\"name\":\"ابهر\"},\n" +
            "{\"id\":203,\"province_id\":14,\"name\":\"خرمدره\"},\n" +
            "{\"id\":204,\"province_id\":14,\"name\":\"زرین آباد(ایجرود)\"},\n" +
            "{\"id\":205,\"province_id\":14,\"name\":\"زنجان\"},\n" +
            "{\"id\":206,\"province_id\":14,\"name\":\"قیدار(خدا بنده)\"},\n" +
            "{\"id\":207,\"province_id\":14,\"name\":\"ماهنشان\"}]},\n" +
            "{\"id\":15,\"name\":\"سمنان\",\"cities\":[{\"id\":208,\"province_id\":15,\"name\":\"ایوانكی\"},\n" +
            "{\"id\":209,\"province_id\":15,\"name\":\"بسطام\"},\n" +
            "{\"id\":210,\"province_id\":15,\"name\":\"دامغان\"},\n" +
            "{\"id\":211,\"province_id\":15,\"name\":\"سرخه\"},\n" +
            "{\"id\":212,\"province_id\":15,\"name\":\"سمنان\"},\n" +
            "{\"id\":213,\"province_id\":15,\"name\":\"شاهرود\"},\n" +
            "{\"id\":214,\"province_id\":15,\"name\":\"شهمیرزاد\"},\n" +
            "{\"id\":215,\"province_id\":15,\"name\":\"گرمسار\"},\n" +
            "{\"id\":216,\"province_id\":15,\"name\":\"مهدیشهر\"}]},\n" +
            "{\"id\":16,\"name\":\"سیستان و بلوچستان\",\"cities\":[{\"id\":217,\"province_id\":16,\"name\":\"ایرانشهر\"},\n" +
            "{\"id\":218,\"province_id\":16,\"name\":\"چابهار\"},\n" +
            "{\"id\":219,\"province_id\":16,\"name\":\"خاش\"},\n" +
            "{\"id\":220,\"province_id\":16,\"name\":\"راسك\"},\n" +
            "{\"id\":221,\"province_id\":16,\"name\":\"زابل\"},\n" +
            "{\"id\":222,\"province_id\":16,\"name\":\"زاهدان\"},\n" +
            "{\"id\":223,\"province_id\":16,\"name\":\"سراوان\"},\n" +
            "{\"id\":224,\"province_id\":16,\"name\":\"سرباز\"},\n" +
            "{\"id\":225,\"province_id\":16,\"name\":\"میرجاوه\"},\n" +
            "{\"id\":226,\"province_id\":16,\"name\":\"نیكشهر\"}]},\n" +
            "{\"id\":17,\"name\":\"فارس\",\"cities\":[{\"id\":227,\"province_id\":17,\"name\":\"آباده\"},\n" +
            "{\"id\":228,\"province_id\":17,\"name\":\"آباده طشك\"},\n" +
            "{\"id\":229,\"province_id\":17,\"name\":\"اردكان\"},\n" +
            "{\"id\":230,\"province_id\":17,\"name\":\"ارسنجان\"},\n" +
            "{\"id\":231,\"province_id\":17,\"name\":\"استهبان\"},\n" +
            "{\"id\":232,\"province_id\":17,\"name\":\"اشكنان\"},\n" +
            "{\"id\":233,\"province_id\":17,\"name\":\"اقلید\"},\n" +
            "{\"id\":234,\"province_id\":17,\"name\":\"اوز\"},\n" +
            "{\"id\":235,\"province_id\":17,\"name\":\"ایج\"},\n" +
            "{\"id\":236,\"province_id\":17,\"name\":\"ایزد خواست\"},\n" +
            "{\"id\":237,\"province_id\":17,\"name\":\"باب انار\"},\n" +
            "{\"id\":238,\"province_id\":17,\"name\":\"بالاده\"},\n" +
            "{\"id\":239,\"province_id\":17,\"name\":\"بنارویه\"},\n" +
            "{\"id\":240,\"province_id\":17,\"name\":\"بهمن\"},\n" +
            "{\"id\":241,\"province_id\":17,\"name\":\"بوانات\"},\n" +
            "{\"id\":242,\"province_id\":17,\"name\":\"بیرم\"},\n" +
            "{\"id\":243,\"province_id\":17,\"name\":\"بیضا\"},\n" +
            "{\"id\":244,\"province_id\":17,\"name\":\"جنت شهر\"},\n" +
            "{\"id\":245,\"province_id\":17,\"name\":\"جهرم\"},\n" +
            "{\"id\":246,\"province_id\":17,\"name\":\"حاجی آباد-زرین دشت\"},\n" +
            "{\"id\":247,\"province_id\":17,\"name\":\"خاوران\"},\n" +
            "{\"id\":248,\"province_id\":17,\"name\":\"خرامه\"},\n" +
            "{\"id\":249,\"province_id\":17,\"name\":\"خشت\"},\n" +
            "{\"id\":250,\"province_id\":17,\"name\":\"خفر\"},\n" +
            "{\"id\":251,\"province_id\":17,\"name\":\"خنج\"},\n" +
            "{\"id\":252,\"province_id\":17,\"name\":\"خور\"},\n" +
            "{\"id\":253,\"province_id\":17,\"name\":\"داراب\"},\n" +
            "{\"id\":254,\"province_id\":17,\"name\":\"رونیز علیا\"},\n" +
            "{\"id\":255,\"province_id\":17,\"name\":\"زاهدشهر\"},\n" +
            "{\"id\":256,\"province_id\":17,\"name\":\"زرقان\"},\n" +
            "{\"id\":257,\"province_id\":17,\"name\":\"سده\"},\n" +
            "{\"id\":258,\"province_id\":17,\"name\":\"سروستان\"},\n" +
            "{\"id\":259,\"province_id\":17,\"name\":\"سعادت شهر\"},\n" +
            "{\"id\":260,\"province_id\":17,\"name\":\"سورمق\"},\n" +
            "{\"id\":261,\"province_id\":17,\"name\":\"ششده\"},\n" +
            "{\"id\":262,\"province_id\":17,\"name\":\"شیراز\"},\n" +
            "{\"id\":263,\"province_id\":17,\"name\":\"صغاد\"},\n" +
            "{\"id\":264,\"province_id\":17,\"name\":\"صفاشهر\"},\n" +
            "{\"id\":265,\"province_id\":17,\"name\":\"علاء مرودشت\"},\n" +
            "{\"id\":266,\"province_id\":17,\"name\":\"عنبر\"},\n" +
            "{\"id\":267,\"province_id\":17,\"name\":\"فراشبند\"},\n" +
            "{\"id\":268,\"province_id\":17,\"name\":\"فسا\"},\n" +
            "{\"id\":269,\"province_id\":17,\"name\":\"فیروز آباد\"},\n" +
            "{\"id\":270,\"province_id\":17,\"name\":\"قائمیه\"},\n" +
            "{\"id\":271,\"province_id\":17,\"name\":\"قادر آباد\"},\n" +
            "{\"id\":272,\"province_id\":17,\"name\":\"قطب آباد\"},\n" +
            "{\"id\":273,\"province_id\":17,\"name\":\"قیر\"},\n" +
            "{\"id\":274,\"province_id\":17,\"name\":\"كازرون\"},\n" +
            "{\"id\":275,\"province_id\":17,\"name\":\"كنار تخته\"},\n" +
            "{\"id\":276,\"province_id\":17,\"name\":\"گراش\"},\n" +
            "{\"id\":277,\"province_id\":17,\"name\":\"لار\"},\n" +
            "{\"id\":278,\"province_id\":17,\"name\":\"لامرد\"},\n" +
            "{\"id\":279,\"province_id\":17,\"name\":\"لپوئی\"},\n" +
            "{\"id\":280,\"province_id\":17,\"name\":\"لطیفی\"},\n" +
            "{\"id\":281,\"province_id\":17,\"name\":\"مبارك آباد دیز\"},\n" +
            "{\"id\":282,\"province_id\":17,\"name\":\"مرودشت\"},\n" +
            "{\"id\":283,\"province_id\":17,\"name\":\"مشكان\"},\n" +
            "{\"id\":284,\"province_id\":17,\"name\":\"مصیر\"},\n" +
            "{\"id\":285,\"province_id\":17,\"name\":\"مهر فارس(گله دار)\"},\n" +
            "{\"id\":286,\"province_id\":17,\"name\":\"میمند\"},\n" +
            "{\"id\":287,\"province_id\":17,\"name\":\"نوبندگان\"},\n" +
            "{\"id\":288,\"province_id\":17,\"name\":\"نودان\"},\n" +
            "{\"id\":289,\"province_id\":17,\"name\":\"نورآباد\"},\n" +
            "{\"id\":290,\"province_id\":17,\"name\":\"نی ریز\"},\n" +
            "{\"id\":291,\"province_id\":17,\"name\":\"کوار\"}]},\n" +
            "{\"id\":18,\"name\":\"قزوین\",\"cities\":[{\"id\":292,\"province_id\":18,\"name\":\"آبیك\"},\n" +
            "{\"id\":293,\"province_id\":18,\"name\":\"البرز\"},\n" +
            "{\"id\":294,\"province_id\":18,\"name\":\"بوئین زهرا\"},\n" +
            "{\"id\":295,\"province_id\":18,\"name\":\"تاكستان\"},\n" +
            "{\"id\":296,\"province_id\":18,\"name\":\"قزوین\"},\n" +
            "{\"id\":297,\"province_id\":18,\"name\":\"محمود آباد نمونه\"}]},\n" +
            "{\"id\":19,\"name\":\"قم\",\"cities\":[{\"id\":298,\"province_id\":19,\"name\":\"قم\"}]},\n" +
            "{\"id\":20,\"name\":\"كردستان\",\"cities\":[{\"id\":299,\"province_id\":20,\"name\":\"بانه\"},\n" +
            "{\"id\":300,\"province_id\":20,\"name\":\"بیجار\"},\n" +
            "{\"id\":301,\"province_id\":20,\"name\":\"دهگلان\"},\n" +
            "{\"id\":302,\"province_id\":20,\"name\":\"دیواندره\"},\n" +
            "{\"id\":303,\"province_id\":20,\"name\":\"سقز\"},\n" +
            "{\"id\":304,\"province_id\":20,\"name\":\"سنندج\"},\n" +
            "{\"id\":305,\"province_id\":20,\"name\":\"قروه\"},\n" +
            "{\"id\":306,\"province_id\":20,\"name\":\"كامیاران\"},\n" +
            "{\"id\":307,\"province_id\":20,\"name\":\"مریوان\"}]},\n" +
            "{\"id\":21,\"name\":\"كرمان\",\"cities\":[{\"id\":308,\"province_id\":21,\"name\":\"بابك\"},\n" +
            "{\"id\":309,\"province_id\":21,\"name\":\"بافت\"},\n" +
            "{\"id\":310,\"province_id\":21,\"name\":\"بردسیر\"},\n" +
            "{\"id\":311,\"province_id\":21,\"name\":\"بم\"},\n" +
            "{\"id\":312,\"province_id\":21,\"name\":\"جیرفت\"},\n" +
            "{\"id\":313,\"province_id\":21,\"name\":\"راور\"},\n" +
            "{\"id\":314,\"province_id\":21,\"name\":\"رفسنجان\"},\n" +
            "{\"id\":315,\"province_id\":21,\"name\":\"زرند\"},\n" +
            "{\"id\":316,\"province_id\":21,\"name\":\"سیرجان\"},\n" +
            "{\"id\":317,\"province_id\":21,\"name\":\"كرمان\"},\n" +
            "{\"id\":318,\"province_id\":21,\"name\":\"كهنوج\"},\n" +
            "{\"id\":319,\"province_id\":21,\"name\":\"منوجان\"}]},\n" +
            "{\"id\":22,\"name\":\"كرمانشاه\",\"cities\":[{\"id\":320,\"province_id\":22,\"name\":\"اسلام آباد غرب\"},\n" +
            "{\"id\":321,\"province_id\":22,\"name\":\"پاوه\"},\n" +
            "{\"id\":322,\"province_id\":22,\"name\":\"تازه آباد- ثلاث باباجانی\"},\n" +
            "{\"id\":323,\"province_id\":22,\"name\":\"جوانرود\"},\n" +
            "{\"id\":324,\"province_id\":22,\"name\":\"سر پل ذهاب\"},\n" +
            "{\"id\":325,\"province_id\":22,\"name\":\"سنقر كلیائی\"},\n" +
            "{\"id\":326,\"province_id\":22,\"name\":\"صحنه\"},\n" +
            "{\"id\":327,\"province_id\":22,\"name\":\"قصر شیرین\"},\n" +
            "{\"id\":328,\"province_id\":22,\"name\":\"كرمانشاه\"},\n" +
            "{\"id\":329,\"province_id\":22,\"name\":\"كنگاور\"},\n" +
            "{\"id\":330,\"province_id\":22,\"name\":\"گیلان غرب\"},\n" +
            "{\"id\":331,\"province_id\":22,\"name\":\"هرسین\"}]},\n" +
            "{\"id\":23,\"name\":\"كهكیلویه و بویراحمد\",\"cities\":[{\"id\":332,\"province_id\":23,\"name\":\"دهدشت\"},\n" +
            "{\"id\":333,\"province_id\":23,\"name\":\"دوگنبدان\"},\n" +
            "{\"id\":334,\"province_id\":23,\"name\":\"سی سخت- دنا\"},\n" +
            "{\"id\":335,\"province_id\":23,\"name\":\"گچساران\"},\n" +
            "{\"id\":336,\"province_id\":23,\"name\":\"یاسوج\"}]},\n" +
            "{\"id\":24,\"name\":\"گلستان\",\"cities\":[{\"id\":337,\"province_id\":24,\"name\":\"آزاد شهر\"},\n" +
            "{\"id\":338,\"province_id\":24,\"name\":\"آق قلا\"},\n" +
            "{\"id\":339,\"province_id\":24,\"name\":\"انبارآلوم\"},\n" +
            "{\"id\":340,\"province_id\":24,\"name\":\"اینچه برون\"},\n" +
            "{\"id\":341,\"province_id\":24,\"name\":\"بندر گز\"},\n" +
            "{\"id\":342,\"province_id\":24,\"name\":\"تركمن\"},\n" +
            "{\"id\":343,\"province_id\":24,\"name\":\"جلین\"},\n" +
            "{\"id\":344,\"province_id\":24,\"name\":\"خان ببین\"},\n" +
            "{\"id\":345,\"province_id\":24,\"name\":\"رامیان\"},\n" +
            "{\"id\":346,\"province_id\":24,\"name\":\"سرخس کلاته\"},\n" +
            "{\"id\":347,\"province_id\":24,\"name\":\"سیمین شهر\"},\n" +
            "{\"id\":348,\"province_id\":24,\"name\":\"علی آباد كتول\"},\n" +
            "{\"id\":349,\"province_id\":24,\"name\":\"فاضل آباد\"},\n" +
            "{\"id\":350,\"province_id\":24,\"name\":\"كردكوی\"},\n" +
            "{\"id\":351,\"province_id\":24,\"name\":\"كلاله\"},\n" +
            "{\"id\":352,\"province_id\":24,\"name\":\"گالیکش\"},\n" +
            "{\"id\":353,\"province_id\":24,\"name\":\"گرگان\"},\n" +
            "{\"id\":354,\"province_id\":24,\"name\":\"گمیش تپه\"},\n" +
            "{\"id\":355,\"province_id\":24,\"name\":\"گنبد كاووس\"},\n" +
            "{\"id\":356,\"province_id\":24,\"name\":\"مراوه تپه\"},\n" +
            "{\"id\":357,\"province_id\":24,\"name\":\"مینو دشت\"},\n" +
            "{\"id\":358,\"province_id\":24,\"name\":\"نگین شهر\"},\n" +
            "{\"id\":359,\"province_id\":24,\"name\":\"نوده خاندوز\"},\n" +
            "{\"id\":360,\"province_id\":24,\"name\":\"نوکنده\"}]},\n" +
            "{\"id\":25,\"name\":\"گیلان\",\"cities\":[{\"id\":361,\"province_id\":25,\"name\":\"آستارا\"},\n" +
            "{\"id\":362,\"province_id\":25,\"name\":\"آستانه اشرفیه\"},\n" +
            "{\"id\":363,\"province_id\":25,\"name\":\"املش\"},\n" +
            "{\"id\":364,\"province_id\":25,\"name\":\"بندرانزلی\"},\n" +
            "{\"id\":365,\"province_id\":25,\"name\":\"خمام\"},\n" +
            "{\"id\":366,\"province_id\":25,\"name\":\"رشت\"},\n" +
            "{\"id\":367,\"province_id\":25,\"name\":\"رضوان شهر\"},\n" +
            "{\"id\":368,\"province_id\":25,\"name\":\"رود سر\"},\n" +
            "{\"id\":369,\"province_id\":25,\"name\":\"رودبار\"},\n" +
            "{\"id\":370,\"province_id\":25,\"name\":\"سیاهكل\"},\n" +
            "{\"id\":371,\"province_id\":25,\"name\":\"شفت\"},\n" +
            "{\"id\":372,\"province_id\":25,\"name\":\"صومعه سرا\"},\n" +
            "{\"id\":373,\"province_id\":25,\"name\":\"فومن\"},\n" +
            "{\"id\":374,\"province_id\":25,\"name\":\"كلاچای\"},\n" +
            "{\"id\":375,\"province_id\":25,\"name\":\"لاهیجان\"},\n" +
            "{\"id\":376,\"province_id\":25,\"name\":\"لنگرود\"},\n" +
            "{\"id\":377,\"province_id\":25,\"name\":\"لوشان\"},\n" +
            "{\"id\":378,\"province_id\":25,\"name\":\"ماسال\"},\n" +
            "{\"id\":379,\"province_id\":25,\"name\":\"ماسوله\"},\n" +
            "{\"id\":380,\"province_id\":25,\"name\":\"منجیل\"},\n" +
            "{\"id\":381,\"province_id\":25,\"name\":\"هشتپر\"}]},\n" +
            "{\"id\":26,\"name\":\"لرستان\",\"cities\":[{\"id\":382,\"province_id\":26,\"name\":\"ازنا\"},\n" +
            "{\"id\":383,\"province_id\":26,\"name\":\"الشتر\"},\n" +
            "{\"id\":384,\"province_id\":26,\"name\":\"الیگودرز\"},\n" +
            "{\"id\":385,\"province_id\":26,\"name\":\"بروجرد\"},\n" +
            "{\"id\":386,\"province_id\":26,\"name\":\"پلدختر\"},\n" +
            "{\"id\":387,\"province_id\":26,\"name\":\"خرم آباد\"},\n" +
            "{\"id\":388,\"province_id\":26,\"name\":\"دورود\"},\n" +
            "{\"id\":389,\"province_id\":26,\"name\":\"سپید دشت\"},\n" +
            "{\"id\":390,\"province_id\":26,\"name\":\"كوهدشت\"},\n" +
            "{\"id\":391,\"province_id\":26,\"name\":\"نورآباد\"}]},\n" +
            "{\"id\":27,\"name\":\"مازندران\",\"cities\":[{\"id\":392,\"province_id\":27,\"name\":\"آمل\"},\n" +
            "{\"id\":393,\"province_id\":27,\"name\":\"بابل\"},\n" +
            "{\"id\":394,\"province_id\":27,\"name\":\"بابلسر\"},\n" +
            "{\"id\":395,\"province_id\":27,\"name\":\"بلده\"},\n" +
            "{\"id\":396,\"province_id\":27,\"name\":\"بهشهر\"},\n" +
            "{\"id\":397,\"province_id\":27,\"name\":\"پل سفید\"},\n" +
            "{\"id\":398,\"province_id\":27,\"name\":\"تنكابن\"},\n" +
            "{\"id\":399,\"province_id\":27,\"name\":\"جویبار\"},\n" +
            "{\"id\":400,\"province_id\":27,\"name\":\"چالوس\"},\n" +
            "{\"id\":401,\"province_id\":27,\"name\":\"خرم آباد\"},\n" +
            "{\"id\":402,\"province_id\":27,\"name\":\"رامسر\"},\n" +
            "{\"id\":403,\"province_id\":27,\"name\":\"رستم کلا\"},\n" +
            "{\"id\":404,\"province_id\":27,\"name\":\"ساری\"},\n" +
            "{\"id\":405,\"province_id\":27,\"name\":\"سلمانشهر\"},\n" +
            "{\"id\":406,\"province_id\":27,\"name\":\"سواد كوه\"},\n" +
            "{\"id\":407,\"province_id\":27,\"name\":\"فریدون كنار\"},\n" +
            "{\"id\":408,\"province_id\":27,\"name\":\"قائم شهر\"},\n" +
            "{\"id\":409,\"province_id\":27,\"name\":\"گلوگاه\"},\n" +
            "{\"id\":410,\"province_id\":27,\"name\":\"محمودآباد\"},\n" +
            "{\"id\":411,\"province_id\":27,\"name\":\"مرزن آباد\"},\n" +
            "{\"id\":412,\"province_id\":27,\"name\":\"نكا\"},\n" +
            "{\"id\":413,\"province_id\":27,\"name\":\"نور\"},\n" +
            "{\"id\":414,\"province_id\":27,\"name\":\"نوشهر\"}]},\n" +
            "{\"id\":28,\"name\":\"مركزی\",\"cities\":[{\"id\":415,\"province_id\":28,\"name\":\"آشتیان\"},\n" +
            "{\"id\":416,\"province_id\":28,\"name\":\"اراك\"},\n" +
            "{\"id\":417,\"province_id\":28,\"name\":\"تفرش\"},\n" +
            "{\"id\":418,\"province_id\":28,\"name\":\"خمین\"},\n" +
            "{\"id\":419,\"province_id\":28,\"name\":\"دلیجان\"},\n" +
            "{\"id\":420,\"province_id\":28,\"name\":\"ساوه\"},\n" +
            "{\"id\":421,\"province_id\":28,\"name\":\"شازند\"},\n" +
            "{\"id\":422,\"province_id\":28,\"name\":\"محلات\"},\n" +
            "{\"id\":423,\"province_id\":28,\"name\":\"کمیجان\"}]},\n" +
            "{\"id\":29,\"name\":\"هرمزگان\",\"cities\":[{\"id\":424,\"province_id\":29,\"name\":\"ابوموسی\"},\n" +
            "{\"id\":425,\"province_id\":29,\"name\":\"انگهران\"},\n" +
            "{\"id\":426,\"province_id\":29,\"name\":\"بستك\"},\n" +
            "{\"id\":427,\"province_id\":29,\"name\":\"بندر جاسك\"},\n" +
            "{\"id\":428,\"province_id\":29,\"name\":\"بندر لنگه\"},\n" +
            "{\"id\":429,\"province_id\":29,\"name\":\"بندرعباس\"},\n" +
            "{\"id\":430,\"province_id\":29,\"name\":\"پارسیان\"},\n" +
            "{\"id\":431,\"province_id\":29,\"name\":\"حاجی آباد\"},\n" +
            "{\"id\":432,\"province_id\":29,\"name\":\"دشتی\"},\n" +
            "{\"id\":433,\"province_id\":29,\"name\":\"دهبارز(رودان)\"},\n" +
            "{\"id\":434,\"province_id\":29,\"name\":\"قشم\"},\n" +
            "{\"id\":435,\"province_id\":29,\"name\":\"كیش\"},\n" +
            "{\"id\":436,\"province_id\":29,\"name\":\"میناب\"}]},\n" +
            "{\"id\":30,\"name\":\"همدان\",\"cities\":[{\"id\":437,\"province_id\":30,\"name\":\"اسدآباد\"},\n" +
            "{\"id\":438,\"province_id\":30,\"name\":\"بهار\"},\n" +
            "{\"id\":439,\"province_id\":30,\"name\":\"تویسركان\"},\n" +
            "{\"id\":440,\"province_id\":30,\"name\":\"رزن\"},\n" +
            "{\"id\":441,\"province_id\":30,\"name\":\"كبودر اهنگ\"},\n" +
            "{\"id\":442,\"province_id\":30,\"name\":\"ملایر\"},\n" +
            "{\"id\":443,\"province_id\":30,\"name\":\"نهاوند\"},\n" +
            "{\"id\":444,\"province_id\":30,\"name\":\"همدان\"}]},\n" +
            "{\"id\":31,\"name\":\"یزد\",\"cities\":[{\"id\":445,\"province_id\":31,\"name\":\"ابركوه\"},\n" +
            "{\"id\":446,\"province_id\":31,\"name\":\"اردكان\"},\n" +
            "{\"id\":447,\"province_id\":31,\"name\":\"اشكذر\"},\n" +
            "{\"id\":448,\"province_id\":31,\"name\":\"بافق\"},\n" +
            "{\"id\":449,\"province_id\":31,\"name\":\"تفت\"},\n" +
            "{\"id\":450,\"province_id\":31,\"name\":\"مهریز\"},\n" +
            "{\"id\":451,\"province_id\":31,\"name\":\"میبد\"},\n" +
            "{\"id\":452,\"province_id\":31,\"name\":\"هرات\"},\n" +
            "{\"id\":453,\"province_id\":31,\"name\":\"یزد\"}]}]";

    public CityNState() {
    }


    /*    public static String getProvinceByCityId() {
            Gson gson = new Gson();
            List<com.example.arsalan.mygym.Objects.CityNState> cityNStateList = gson.fromJson(cityJson, new TypeToken<List<com.example.arsalan.mygym.Objects.CityNState>>() {
            }.getType());
            List<com.example.arsalan.mygym.Objects.Province> provinceList=new ArrayList<>();
            int i=0;
            for (com.example.arsalan.mygym.Objects.CityNState c:cityNStateList
                 ) {
                provinceList.add(new com.example.arsalan.mygym.Objects.Province(c.getTrainerWorkoutPlanId(),c.getProvinceName()));
                i++;
            }
            return provinceList.toString();

        }*/
    public static List<Province> getProvinceList() {
        Gson gson = new Gson();
        return gson.fromJson(cityJson, new TypeToken<List<Province>>() {
        }.getType());
    }

    public static Province getProvinceByCityId(long id) {
        for (Province p :
                getProvinceList()) {
            for (City c :
                    p.cities) {
                if (c.getId() == id) return p;
            }
        }
        return null;
    }

    public static Province getProvinceById(long id) {
        for (Province p :
                getProvinceList()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static City getCity(long id) {
        List<City> cityList = new ArrayList<>();
        for (Province p :
                getProvinceList()) {
            cityList.addAll(p.getCities());
        }
        for (City c : cityList) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

}
