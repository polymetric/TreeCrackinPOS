// tree 0
if ((((seed *     25214903917LU +              11LU) >> 44) & 15) !=  0) return ; // pos Z
if ((((seed * 233752471717045LU +  11718085204285LU) >> 46) &  3) !=  3) return ; // height
if ((((seed *  55986898099985LU +  49720483695876LU) >> 47) &  1) !=  1) return ; // base height
if ((((seed *  76790647859193LU +  25707281917278LU) >> 47) &  1) !=  0) return ; // initial radius
if (((((seed * 205749139540585LU +    277363943098LU) & 281474976710655LU) >> 17) %  3) ==  0) return ; // type
