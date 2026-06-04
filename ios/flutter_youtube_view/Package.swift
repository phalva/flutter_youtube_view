// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "flutter_youtube_view",
    platforms: [
        .iOS("14.0")
    ],
    products: [
        .library(name: "flutter-youtube-view", targets: ["flutter_youtube_view"])
    ],
    dependencies: [
        .package(url: "https://github.com/rinov/YoutubeKit.git", from: "0.13.0")
    ],
    targets: [
        .target(
            name: "flutter_youtube_view",
            dependencies: [
                .product(name: "YoutubeKit", package: "YoutubeKit")
            ],
            path: "Sources/flutter_youtube_view",
            resources: [
                .process("Resources")
            ],
            publicHeadersPath: "."
        )
    ]
)
