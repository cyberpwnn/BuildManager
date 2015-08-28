# BuildManager
Build Constructivley, not destructivley! With the BuildManager, you can assign multiple builders different builds with descriptions, duedates, locations, warps and more! Only Build Managers can create builders, which allows control. The build manager will also notify the apropriate members of information that is important to them. For example, builders should know when their due date or location is changed, however, if they where not assigned to that build, there is no point for them to know. 
The Build Manager uses the chat Json features a lot, meaning you can click on buttons to navigate where you want. This makes it very easy to use without the hassle of too many commands.

#Downloads
[RELEASE v1.0] (https://github.com/danielmills/BuildManager/blob/master/build/BuildManager.jar?raw=true)

## Changelog
Here, you will be able to see what is going on with Build Manager. WARNING! IF YOU GET A NEW VERSION, ENSURE IT IS MARKED STABLE. There is a chance of object serialization issues, causing all of your data to be wiped!

### BuildManager v1.0
This is the initial release, however it is NOT STABLE, and should only be used for testing purposes. Internally, it can harm only its own data, meaning you should probobly not set important planned builds just yet. However it will not, and has been tested to mot, lose data on actual world files or anything. We are talking about just the data BuildManager Stores
* Data Compression Exponentially decreases file size
* Managers and Builders are notified when something is important to them
* Due Dates fixed and now will require you to set the date with MM/DD/YY, or it will not set a date
* The BuildManager keeps an updated excel spreadsheet with the BMC file, however, it will not read changes. Modifying this will do noting, and will be written over the next time data is saved.
