if ((((seed *     25214903917LU +              11LU) >> 44) & 15) != 11) return 0; // pos X
if ((((seed * 205749139540585LU +    277363943098LU) >> 44) & 15) !=  2) return 0; // pos Z
if ((((seed *  55986898099985LU +  49720483695876LU) >> 46) &  3) !=  0) return 0; // height
if ((((seed * 120950523281469LU + 102626409374399LU) >> 47) &  1) !=  1) return 0; // base height
if ((((seed *  76790647859193LU +  25707281917278LU) >> 47) &  1) !=  0) return 0; // radius
if ((((seed *  61282721086213LU +  25979478236433LU) >> 47) &  1) !=  0) return 0; // initial radius
if (((((seed * 233752471717045LU +  11718085204285LU) & 281474976710655LU) >> 17) %  3) ==  0) return 0; // type
