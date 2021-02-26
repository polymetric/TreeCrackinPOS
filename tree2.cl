if ((((seed * 205749139540585LU +    277363943098LU) >> 46) &  3) !=  2) return 0; // height
if ((((seed * 233752471717045LU +  11718085204285LU) >> 47) &  1) !=  1) return 0; // base height
if ((((seed *  55986898099985LU +  49720483695876LU) >> 47) &  1) !=  0) return 0; // radius
if ((((seed * 120950523281469LU + 102626409374399LU) >> 47) &  1) !=  0) return 0; // initial radius
if (((((seed *     25214903917LU +              11LU) & 281474976710655LU) >> 17) %  3) ==  0) return 0; // type
