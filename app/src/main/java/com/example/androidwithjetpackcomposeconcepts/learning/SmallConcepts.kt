package com.example.androidwithjetpackcomposeconcepts.learning

// The @JvmStatic @JvmOverloads @JvmField annotations are used in kotlin to control how your code interacts
// with java virtual machine when you kotlin code compiles to bytecode

// They help to bridge the gap between java and kotlin as they both are interoperable

// @JvmStatic field is used for properties of class that are inside companion objects because that can not be accessed directly from java as static so making them static

// @JvmOverloads field is used for default parameters functionality in kotlin ,as in java there is no such concept called default parameters

// @JvmField is used for accessing kotlin class properties from java

// inline keyword is used to instruct the compiler to insert the whole function code where it is being called
// it improves the performance by reducing the overhead of function call overhead
//
// noinline keyword is used inside function parameters that is already inline ,to instruct the compiler to not insert the code of that higher order function

// kotlin scope functions are the functions that do some work inside the scope of an object
// there are 5 types of these scope functions
// let, run , with, also , apply
// the difference in these is how they get the object reference inside the scope and the return statement
// run , with , apply reference the context object by "this"
// let and also -> it
//
// apply and also return the context object.
//
// let, run, and with return the lambda result.

// MVVM is an architectural design pattern designed to separate the concerns of our application
// Model , Views and ViewModels


