Sabina Smajlaj
ss3912
Assignment #9

Part1:(a) 
	BASIC4TRACE: (0xbffff9a0)->MyString(const char *) -- MyString s1("one"); //The "one" char is read in 
	BASIC4TRACE: (0xbffff998)->MyString(const char *) -- MyString s2("two"); //The "two" char is read in
	BASIC4TRACE: (0xbffff9b0)->MyString(const MyString&) --  MyString s3 = add(s1, s2);
	BASIC4TRACE: (0xbffff9b8)->MyString(const MyString&) -- MyString s3 = add(s1, s2);
	BASIC4TRACE: (0xbffff948)->MyString(const char *) --   MyString temp(" and "); //in the "add" function, a char "and" is read in
	BASIC4TRACE: op+(const MyString&, const MyString&) --  return s1 + temp + s2; // In the "add" function, s1 and temp are evaulated first 
	BASIC4TRACE: (0xbffff8f8)->MyString() -- MyString temp; //in operator+ 
	BASIC4TRACE: (0xbffff958)->MyString(const MyString&) -- return temp; //copying return to place where it wont be deleted 
	BASIC4TRACE: (0xbffff8f8)->˜MyString() -- deleting temporary variable 
	BASIC4TRACE: op+(const MyString&, const MyString&) -- return s1 + temp + s2; // In the "add" function, evaluate temp2 and s2 and put it into temp3 
	BASIC4TRACE: (0xbffff8f8)->MyString() -- MyString temp; //in operator+ 
	BASIC4TRACE: (0xbffff950)->MyString(const MyString&) --   return temp; //copying return to place where it wont be deleted 
	BASIC4TRACE: (0xbffff8f8)->˜MyString() -- deleting temporary variable 

	BASIC4TRACE: (0xbffff9a8)->MyString(const MyString&) -- return s1 + temp + s2; //complier wants to place this into somewhere where it wont be lost 
	BASIC4TRACE: (0xbffff950)->˜MyString() -- deleting temp 
	BASIC4TRACE: (0xbffff958)->˜MyString() -- deleteing temp2
	BASIC4TRACE: (0xbffff948)->˜MyString() -- deleteing temp3
	BASIC4TRACE: (0xbffff990)->MyString(const MyString&) -- MyString s3 = add(s1, s2);
	BASIC4TRACE: (0xbffff9a8)->˜MyString() --  deleting result of add(s1, s2) 
	BASIC4TRACE: (0xbffff9b8)->˜MyString() -- deleting copied value of s1
	BASIC4TRACE: (0xbffff9b0)->˜MyString() -- deleting copied value of s2
	one and two
	BASIC4TRACE: (0xbffff990)->˜MyString() --  deleting s3 
	BASIC4TRACE: (0xbffff998)->˜MyString() --  deleting s1
	BASIC4TRACE: (0xbffff9a0)->˜MyString() --  deleting s2 

(b) 

BASIC4TRACE: (0x7fff0fc8dce0)->MyString(const char *)
BASIC4TRACE: (0x7fff0fc8dcf0)->MyString(const char *)
BASIC4TRACE: (0x7fff0fc8dc90)->MyString(const char *)
BASIC4TRACE: op+(const MyString&, const MyString&)
BASIC4TRACE: (0x7fff0fc8dc30)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dc40)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dca0)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dc40)->~MyString()
BASIC4TRACE: (0x7fff0fc8dc30)->~MyString()
BASIC4TRACE: op+(const MyString&, const MyString&)
BASIC4TRACE: (0x7fff0fc8dc30)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dc40)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dcb0)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dc40)->~MyString()
BASIC4TRACE: (0x7fff0fc8dc30)->~MyString()
BASIC4TRACE: (0x7fff0fc8dd10)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dcb0)->~MyString()
BASIC4TRACE: (0x7fff0fc8dca0)->~MyString()
BASIC4TRACE: (0x7fff0fc8dc90)->~MyString()
BASIC4TRACE: (0x7fff0fc8dd00)->MyString(const MyString&)
BASIC4TRACE: (0x7fff0fc8dd10)->~MyString()
one and two
BASIC4TRACE: (0x7fff0fc8dd00)->~MyString()
BASIC4TRACE: (0x7fff0fc8dcf0)->~MyString()
BASIC4TRACE: (0x7fff0fc8dce0)->~MyString()

s1 and s2 are references instead of actual values. This saves having to copy the value each time we need it. We hence don't need to copy the value to a temporary variable and also don't have to destroy it. 


(c) The flag is used to turn off return value optimazation. RVO is a technique used by the complier to eliminate the temporary object created to hold a function's return value; after the function is called, this value is no longer available. This technique is used to increase speed. However, the flag disables this feature, allowing the return value to remain. 

BASIC4TRACE: (0x7fff414270e0)->MyString(const char *)
BASIC4TRACE: (0x7fff414270f0)->MyString(const char *)
BASIC4TRACE: (0x7fff414270a0)->MyString(const char *)
BASIC4TRACE: op+(const MyString&, const MyString&)
BASIC4TRACE: (0x7fff41427050)->MyString(const MyString&)
BASIC4TRACE: (0x7fff414270b0)->MyString(const MyString&)
BASIC4TRACE: (0x7fff41427050)->~MyString()
BASIC4TRACE: op+(const MyString&, const MyString&)
BASIC4TRACE: (0x7fff41427050)->MyString(const MyString&)
BASIC4TRACE: (0x7fff41427100)->MyString(const MyString&)
BASIC4TRACE: (0x7fff41427050)->~MyString()
BASIC4TRACE: (0x7fff414270b0)->~MyString()
BASIC4TRACE: (0x7fff414270a0)->~MyString()
one and two
BASIC4TRACE: (0x7fff41427100)->~MyString()
BASIC4TRACE: (0x7fff414270f0)->~MyString()
BASIC4TRACE: (0x7fff414270e0)->~MyString()

Here, we are not creating temporary objects for s1, s2 and s3. We need to only delete the actual values of s1, s2 and s3, which we do here:
BASIC4TRACE: (0x7fff41427100)->~MyString()
BASIC4TRACE: (0x7fff414270f0)->~MyString()
BASIC4TRACE: (0x7fff414270e0)->~MyString()

And of temp, temp2, and temp3:
BASIC4TRACE: (0x7fff41427050)->~MyString()
BASIC4TRACE: (0x7fff414270b0)->~MyString()
BASIC4TRACE: (0x7fff414270a0)->~MyString()

Part2 works as specified. Please excuse it being a few minutes late. I forgot that you have to "git add" files every time they are changed, and hence kept submitting unchanged files. Please view this code! I worked very hard on it!

