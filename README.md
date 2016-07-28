# HoneyComb - Fault Injector Tool 

During the deployment of hundreds of instances on cloud, there could be a number of things that could go wrong and affect the deployment. When you use some of the continuous deployment tools like Spinnaker, it is important to handle some of these faults which cannot be automatically handled by the tool. Also, in order to test the robustness of you deployment pipeline, you need a fault injector to inject faults during the deployment and observe the response. Manual injection of faults for such scenarios could be a time consuming and laborious job.

In order to speed up and automate the process of fault injection, we have developed a Fault Injector tool that is capable of inserting a variety of faults in the deployment process. Custom faults can be developed and this application has the capability to include them without re-compilation. Currently the aim is to deal with Amazon Web Services as the cloud provider. The system is designed in a way that in future extensions can be made in terms of adding more fault generation scenario or supporting other cloud providers.The tool provides accessible APIs so that it can be used as a part of a testing script and also provide an interface for a user to manually select and inject the selected faults into the system.

All other relevent instructions like installation, usage, fault development and CI/CD setup can be found in the [wiki page](https://github.com/s-ashwinkumar/HoneyComb/wiki) of this repository.



# License

Original work Copyright (c) 2015-16 MSIT-Team Honeycomb, Carnegie Mellon University

    Permission is hereby granted, free of charge, to any person obtaining
    a copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software
    is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
    OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
    CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
    OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
