{ pkgs ? import <nixpkgs> {}, ... }:
pkgs.mkShell {
  packages = with pkgs; [
    scala
    sbt
  ];

  shellHook = ''
    export PS1="\[\e[31m\][\[\e[m\]\[\e[35m\]l\[\e[m\]\[\e[33m\]m\[\e[m\]\[\e[32m\]a\[\e[m\]\[\e[36m\]o\[\e[m\]\[\e[34m\]>\[\e[m\] "
  '';

  # TODO: move the ivy2 package cache here so it doesn't mess up the home dir.
}
