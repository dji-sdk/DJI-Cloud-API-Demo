# 关于DJI Cloud API Demo 终止维护公告

发布日期：2025年4月10日

1.项目终止维护说明
即日起，大疆创新(DJI)将停止对DJI Cloud API Demo (地址：https://github.com/dji-sdk/Cloud-API-Demo-Web、https://github.com/dji-sdk/DJI-Cloud-API-Demo）示例项目的更新与技术支持。
该项目作为官方提供的云端集成参考实现，旨在辅助开发者理解API调用逻辑。并非生产级解决方案，可能存在未修复的安全隐患（如数据泄露、未授权访问等）。请避免在生产环境中直接使用Demo中的代码，若直接使用我们强烈建议您启动安全自查，或避免将基于该Demo的服务暴露于公网环境。

2.免责声明
因直接使用Demo代码导致的业务损失、数据风险或第三方纠纷，DJI将不承担任何责任。

3.后续支持
如有疑问，请联系DJI开发者支持团队（邮箱：developer@dji.com)或访问大疆开发者社区获取最新技术资源。
感谢您一直以来的理解与支持！

# DJI Cloud API

## What is the DJI Cloud API?

The launch of the Cloud API mainly solves the problem of developers reinventing the wheel. For developers who do not need in-depth customization of APP, they can directly use DJI Pilot2 to communicate with the third cloud platform, and developers can focus on the development and implementation of cloud service interfaces. 

## Docker

If you don't want to install the development environment, you can try deploying with docker. [Click the link to download.](https://terra-sz-hc1pro-cloudapi.oss-cn-shenzhen.aliyuncs.com/c0af9fe0d7eb4f35a8fe5b695e4d0b96/docker/cloud_api_sample_docker.zip)

## Usage

For more documentation, please visit the [DJI Developer Documentation](https://developer.dji.com/doc/cloud-api-tutorial/cn/).

## Latest Release

Cloud API 1.10.0 was released on 7 Apr 2024. For more information, please visit the [Release Note](https://developer.dji.com/doc/cloud-api-tutorial/cn/).

## License

Cloud API is MIT-licensed. Please refer to the LICENSE file for more information.

